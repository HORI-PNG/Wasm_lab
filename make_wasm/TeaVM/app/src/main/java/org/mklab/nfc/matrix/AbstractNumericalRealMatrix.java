/**
 * Copyright (C) 2021 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.eig.EigenSolution;
import org.mklab.nfc.eig.EigenSolutionElements;
import org.mklab.nfc.eig.QZDecompositionElements;
import org.mklab.nfc.eig.RealEigenSolver;
import org.mklab.nfc.eig.RealGeneralizedEigenSolver;
import org.mklab.nfc.eig.RealQZDecomposer;
import org.mklab.nfc.eig.RealQZDecomposition;
import org.mklab.nfc.fft.ComplexFFTAnalyzer;
import org.mklab.nfc.fft.RealFFTAnalyzer;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;

/**
 * @author koga
 * @version $Revision$, 2021/08/19
 * @param <RS> スカラーの型
 * @param <RM> 行列の型
 * @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 */
public abstract class AbstractNumericalRealMatrix<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends AbstractNumericalRealMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> extends AbstractNumericalMatrix<RS,RM> implements RealNumericalMatrix<RS, RM, CS, CM> {
  /** */
  private static final long serialVersionUID = 6383914813923701550L;

  /**
   * Creates {@link AbstractNumericalRealMatrix}.
   * @param rowSize row size
   * @param columnSize column size
   * @param elements elements
   */
  public AbstractNumericalRealMatrix(int rowSize, int columnSize, RS[][] elements) {
    super(rowSize, columnSize, elements);
  }
  
  /**
   * 新しく生成された<code>BaseNumericalMatrix</code>オブジェクトを初期化します。
   * @param elements 成分
   */
  public AbstractNumericalRealMatrix(final RS[] elements) {
    super(elements);
  }

  /**
   * elementsで与えられた成分を持つ数値行列を生成します。
   * 
   * @param elements 成分
   */
  public AbstractNumericalRealMatrix(final RS[][] elements) {
    super(elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CM eigenValue() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final RealEigenSolver<RS,RM,CS,CM> eigenSolver = new RealEigenSolver<>();
    final CS[] values = eigenSolver.getEigenValue(getElements());
    
    final CS[][] eigenValues = GridUtil.transpose(values);
    return eigenValues[0][0].createGrid(eigenValues);
  }

  /**
   * {@inheritDoc}
   */
  public CM eigenValue(final RM b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (getRowSize() != b.getRowSize()) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final RealGeneralizedEigenSolver<RS,RM,CS,CM> eigenSolver = new RealGeneralizedEigenSolver<>();
    final CS[] values = eigenSolver.getEigenValue(getElements(), b.getElements());
    
    CS[][] eigenValues = GridUtil.transpose(values);
    return eigenValues[0][0].createGrid(eigenValues);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM eigenVector() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final RealEigenSolver<RS,RM,CS,CM> eigenSolver = new RealEigenSolver<>();
    final CS[][] vectors = eigenSolver.getEigenVector(getElements());
    return vectors[0][0].createGrid(vectors);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM eigenVector(final RM b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    } 
    if (getRowSize() != b.getRowSize()) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final CS[][] vectors = new RealGeneralizedEigenSolver<RS,RM,CS,CM>().getEigenVector(getElements(), b.getElements());
    return vectors[0][0].createGrid(vectors);
  }
  
  /**
   * {@inheritDoc}
   */
  public EigenSolution<RS,RM,CS,CM> eigenDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final RealEigenSolver<RS,RM,CS,CM> eigenSolver = new RealEigenSolver<>();
    final EigenSolutionElements<RS,RM,CS,CM> eig = eigenSolver.solve(getElements());

    final RS[][] realValues = GridUtil.diagonal(eig.getReValue());
    final RS[][] imagValues = GridUtil.diagonal(eig.getImValue());
    final RM realValue = realValues[0][0].createGrid(realValues);
    final RM imagValue = imagValues[0][0].createGrid(imagValues);
    
    final RS[][] realVectors = eig.getReVector();
    final RS[][] imagVectors = eig.getImVector();
    final RM realVector = realVectors[0][0].createGrid(realVectors);
    final RM imagVector = imagVectors[0][0].createGrid(imagVectors);

    return new EigenSolution<>(realValue, imagValue,  realVector, imagVector);
  }

  /**
   * {@inheritDoc}
   */
  public EigenSolution<RS,RM,CS,CM> eigenDecompose(final RM b) {
    if (isSquare() == false || b.isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    if (hasSameRowSize(b) == false) {
      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
    }

    final EigenSolutionElements<RS,RM,CS,CM> eig = new RealGeneralizedEigenSolver<RS,RM,CS,CM>().solve(getElements(), b.getElements());
    final RS[][] realValues = GridUtil.diagonal(eig.getReValue());
    final RS[][] imagValues = GridUtil.diagonal(eig.getImValue());
    final RM realValue = realValues[0][0].createGrid(realValues);
    final RM imagValue = imagValues[0][0].createGrid(imagValues);
    
    final RS[][] realVectors = eig.getReVector();
    final RS[][] imagVectors = eig.getImVector();
    final RM realVector = realVectors[0][0].createGrid(realVectors);
    final RM imagVector = imagVectors[0][0].createGrid(imagVectors);

    return new EigenSolution<>(realValue, imagValue, realVector, imagVector);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM fft(final int dataSize) {
    if (getRowSize() == 1) {
      final RS[] re = getElements()[0];
      final RS[] im = createZero().getElements()[0];
      final RS[][] ans = ComplexFFTAnalyzer.fft(re, im, dataSize);
      final CS[] ansComplex = ans[0][0].createComplexArray(ans[0],ans[1]);
      //final CS[] ansComplex = BaseNumericalMatrixUtil.createComplexArray(ans[0], ans[1]);
      return ansComplex[0].createGrid(ansComplex);
    } 
    
    if (getColumnSize() == 1) {
      return this.transpose().fft(dataSize).transpose();
    } 

    throw new MatrixSizeException(Messages.getString("NumericalMatrix.12")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public CM fftRowWise(final int dataSize) {
    final CS[][] ansComplex = fftRowWise(getElements(), dataSize);
    return ansComplex[0][0].createGrid(getRowSize(), getColumnSize(), ansComplex);
  }

  /**
   * 行毎に<code>dataSize</code>点まで,基底が 2 の高速フーリエ変換を計算します。
   * 
   * <p><code>dataSize</code>は2のべき乗でなければならない。
   * 
   * @param data データ
   * @param dataSize データの個数
   * @return フーリエ変換の結果
   */
  private CS[][] fftRowWise(final RS[][] data, final int dataSize) {
    final int localRowSize = data.length;
    final int localColumnSize = (localRowSize == 0 || data[0] == null) ? 0 : data[0].length;
    
    final RS scalar = data[0][0];
    final RS[][] ansr = scalar.createArray(localRowSize, localColumnSize);
    final RS[][] ansi = scalar.createArray(localRowSize, localColumnSize);

    for (int i = 0; i < localRowSize; i++) {
      final RS[][] tmp = RealFFTAnalyzer.fft(data[i], dataSize);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }

    final CS[][] ansComplex = ansr[0][0].createComplexArray(ansr, ansi);
    //final CS[][] ansComplex = BaseNumericalMatrixUtil.createComplexArray(ansr, ansi);
    return ansComplex;
  }

  /**
   * {@inheritDoc}
   */
  public CM fft() {
    return fft(Math.max(getRowSize(), getColumnSize()));
  }

  /**
   * {@inheritDoc}
   */
  public CM fftRowWise() {
    return fftRowWise(getColumnSize());
  }
  
  /**
   * {@inheritDoc}
   */
  public CM fftColumnWise(final int dataSize) {
    return this.transpose().fftRowWise(dataSize).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public CM fftColumnWise() {
    return fftColumnWise(getRowSize());
  }

  /**
   * {@inheritDoc}
   */
  public CM ifftRowWise(final int dataSize) {
//    if (dataSize - Math.pow(2, Math.log(dataSize) / Math.log(2)) != 0 || dataSize == 1) {
//      throw new MatrixSizeException(Messages.getString("NumericalMatrix.14")); //$NON-NLS-1$
//    }

    final RS scalar = getElement(1, 1);

    final RS[][] ansr = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] ansi = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] mr = getElements();
    final RS[][] mi = createZero().getElements(); 

    for (int i = 0; i < getRowSize(); i++) {
      final RS[][] tmp = ComplexFFTAnalyzer.ifft(mr[i], mi[i], dataSize);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }

    final CS[][] ansComplex = ansr[0][0].createComplexArray(ansr, ansi);
    //final CS[][] ansComplex = BaseNumericalMatrixUtil.createComplexArray(ansr, ansi);
    return ansComplex[0][0].createGrid(getRowSize(), getColumnSize(), ansComplex);
  }

  /**
   * {@inheritDoc}
   */
  public CM ifft(final int dataSize) {
    if (getRowSize() == 1) {
//      if (dataSize - Math.pow(2, Math.log(dataSize) / Math.log(2)) != 0 || dataSize == 1) {
//        throw new MatrixSizeException(Messages.getString("NumericalMatrix.15")); //$NON-NLS-1$
//      }

      final RS[] re = getElements()[0];
      final RS[] im =createZero().getElements()[0];
      final RS[][] ans = ComplexFFTAnalyzer.ifft(re, im, dataSize);
      final CS[] ansComplex = ans[0][0].createComplexArray(ans[0], ans[1]);
      //final CS[] ansComplex = BaseNumericalMatrixUtil.<S,M> createComplexArray(ans[0], ans[1]);
      return ansComplex[0].createGrid(ansComplex);
    }
    
    if (getColumnSize() == 1) {
      return transpose().ifft(dataSize).transpose();
    } 

    throw new MatrixSizeException(Messages.getString("NumericalMatrix.16")); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  public CM ifft() {
    return ifft(Math.max(getRowSize(), getColumnSize()));
  }

  /**
   * {@inheritDoc}
   */
  public CM ifftColumnWise(final int dataSize) {
    return this.transpose().ifftRowWise(dataSize).transpose();
  }

  /**
   * {@inheritDoc}
   */
  public CM ifftRowWise() {
    return ifftRowWise(getColumnSize());
  }

  /**
   * {@inheritDoc}
   */
  public CM ifftColumnWise() {
    return ifftColumnWise(getRowSize());
  }

  /**
   * {@inheritDoc}
   */
  public RealQZDecomposition<RS,RM,CS,CM> qzDecompose(final RM b) {
    final QZDecompositionElements<RS,RM> qz = new RealQZDecomposer<RS,RM>().decompose(getElements(), b.getElements());
    RS[][] aaa = qz.getAA();
    final RM aa =aaa[0][0].createGrid(aaa);
    RS[][] bbb = qz.getBB();
    final RM bb = bbb[0][0].createGrid(aaa);
    RS[][] qqq = qz.getQ();
    final RM q =qqq[0][0].createGrid(aaa);
    RS[][] zzz = qz.getZ();
    final  RM z = zzz[0][0].createGrid(aaa);
    RS[][] xxxRe = qz.getReX();
    final RM xReal = xxxRe[0][0].createGrid(aaa);
    RS[][] xxxIm = qz.getImX();
    final RM xImag = xxxIm[0][0].createGrid(aaa);
    final CM x = xReal.createComplex(xReal, xImag);
    return new RealQZDecomposition<>(aa, bb, q, z, x);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM sqrt() {
    return createComplex((RM)this, this.createZero()).sqrt();
  }
  
  /**
   * {@inheritDoc}
   */
  public CM toComplex() {
    RS[][] elements = getElements();
    CS[][] ans = elements[0][0].toComplex().createArray(getRowSize(), getColumnSize());
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans[i][j] = elements[i][j].toComplex();
      }
    }
    
    return ans[0][0].createGrid(ans);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM createComplex(RM realPart, RM imagPart) {
    return realPart.toComplex().create(realPart, imagPart);
  }

  /**
   * {@inheritDoc}
   */
  public CM add(CM value) {
    return toComplex().add(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM subtract(CM value) {
    return toComplex().subtract(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM multiply(CM value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(CM value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(CM value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM multiply(CS value) {
    return toComplex().multiply(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(CS value) {
    return toComplex().divide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(CS value) {
    return toComplex().leftDivide(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM appendDown(CM value) {
    return toComplex().appendDown(value);
  }

  /**
   * {@inheritDoc}
   */
  public CM appendRight(CM value) {
    return toComplex().appendRight(value);
  }
}
