/*
 * Created on 2008/03/16
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.eig.ComplexEigenSolver;
import org.mklab.nfc.eig.ComplexHessenbergDecomposer;
import org.mklab.nfc.eig.ComplexQRDecomposer;
import org.mklab.nfc.eig.ComplexQZDecomposition;
import org.mklab.nfc.eig.ComplexSchurDecomposer;
import org.mklab.nfc.eig.EigenSolution;
import org.mklab.nfc.eig.EigenSolutionElements;
import org.mklab.nfc.eig.HessenbergDecomposition;
import org.mklab.nfc.eig.HessenbergDecompositionElements;
import org.mklab.nfc.eig.QRDecomposition;
import org.mklab.nfc.eig.QRDecompositionElements;
import org.mklab.nfc.eig.SchurDecomposition;
import org.mklab.nfc.eig.SchurDecompositionElements;
import org.mklab.nfc.fft.ComplexFFTAnalyzer;
import org.mklab.nfc.scalar.AbstractComplexNumericalScalar;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.svd.ComplexSingularValueDecomposer;
import org.mklab.nfc.svd.SingularValueDecomposition;
import org.mklab.nfc.svd.SingularValueDecompositionElements;


/**
 * {@link AbstractComplexNumericalScalar}を成分とする行列を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.7 $, 2008/03/16
 * @param <RS> 実スカラーの型
 * @param <RM> 実行列の型
 * @param <CS> 複素スカラーの型 
 * @param <CM> 複素行列の型
 */
public abstract class AbstractNumericalComplexMatrix<RS extends RealNumericalScalar<RS, RM,CS,CM>, RM extends RealNumericalMatrix<RS, RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends AbstractNumericalComplexMatrix<RS,RM,CS,CM>> extends AbstractNumericalMatrix<CS, CM> implements ComplexNumericalMatrix<RS,RM,CS,CM>{

  /** シリアル番号。 */
  private static final long serialVersionUID = 2919182055081619403L;

  /**
   * 
   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   */
  public AbstractNumericalComplexMatrix(final CS[] elements) {
    super(elements);
  }

//  /**
//   * 
//   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   * @param imagElements 虚部セリ分
//   */
//  public BaseNumericalComplexMatrix(final RS[] realElements, final RS[] imagElements) {
//    this(BaseNumericalMatrixUtil.createComplexArray(realElements, imagElements));
//  }
//
//  /**
//   * 
//   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   */
//  public BaseNumericalComplexMatrix(final RS[] realElements) {
//    this(BaseNumericalMatrixUtil.createComplexArray(realElements));
//  }
//
//    /**
//     * 
//     * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
//     * 
//     * @param realElements 実部成分
//     * @param imagElements 虚部セリ分
//     */
//    public BaseNumericalComplexMatrix(final RM realElements, final RM imagElements) {
//      this(BaseNumericalMatrixUtil.createComplexArray(realElements.getElements(), imagElements.getElements()));
//    }
  
  /**
   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   */
  public AbstractNumericalComplexMatrix(final CS[][] elements) {
    super(elements);
  }

//  /**
//   * 
//   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   * @param imagElements 虚部セリ分
//   */
//  public BaseNumericalComplexMatrix(final RS[][] realElements, final RS[][] imagElements) {
//    this(BaseNumericalMatrixUtil.createComplexArray(realElements, imagElements));
//  }
//
//  /**
//   * 
//   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   */
//  public BaseNumericalComplexMatrix(final RS[][] realElements) {
//    this(BaseNumericalMatrixUtil.createComplexArray(realElements));
//  }

  /**
   * 新しく生成された<code>NumericalComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分
   */
  public AbstractNumericalComplexMatrix(final int rowSize, final int columnSize, final CS[][] elements) {
    super(rowSize, columnSize, elements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HessenbergDecomposition<CS, CM> hessenbergDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final HessenbergDecompositionElements<CS, CM> qh = new ComplexHessenbergDecomposer<RS,RM,CS, CM>().decompose(getElements());
    final CS[][] qq = qh.getQ();
    final CS[][] hh = qh.getH();
    final CM q = qq[0][0].createGrid(qq);
    final CM h = hh[0][0].createGrid(hh);

    return new HessenbergDecomposition<>(q, h);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SchurDecomposition<CS, CM> schurDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final SchurDecompositionElements<CS, CM> ut = new ComplexSchurDecomposer<RS, RM,CS,CM>().decompose(getElements());
    CS[][] uu = ut.getU();
    CS[][] tt = ut.getT();
    final CM u = uu[0][0].createGrid(uu);
    final CM t = tt[0][0].createGrid(tt);

    return new SchurDecomposition<>(u, t);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QRDecomposition<CS, CM> qrDecompose() {
    final QRDecompositionElements<CS, CM> qr = new ComplexQRDecomposer<RS, RM,CS,CM>().decompose(getElements());
    CS[][] qq = qr.getQ();
    CS[][] rr = qr.getR();
    final CM q = qq[0][0].createGrid(qq);
    final CM r = rr[0][0].createGrid(rr);
    return new QRDecomposition<>(q, r);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QRDecomposition<CS, CM> qrDecomposeWithPermutation() {
    final QRDecompositionElements<CS, CM> qrp = new ComplexQRDecomposer<RS, RM,CS,CM>().decomposeWithPermutation(getElements());
    CS[][] qq = qrp.getQ();
    CS[][] rr = qrp.getR();
    final CM q = qq[0][0].createGrid(qq);
    final CM r = rr[0][0].createGrid(rr);
    final IntMatrix p = new IntMatrix(qrp.getP());
    return new QRDecomposition<>(q, r, p);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EigenSolution<RS, RM,CS,CM> eigenDecompose() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }
    final ComplexEigenSolver<RS, RM,CS,CM> eigenSolver = new ComplexEigenSolver<>();
    final EigenSolutionElements<RS, RM,CS,CM> eig = eigenSolver.solve(getElements());

    final RS[][] realValues = GridUtil.diagonal(eig.getReValue());
    final RS[][] imagValues = GridUtil.diagonal(eig.getImValue());

    final RM realValue = realValues[0][0].createGrid(realValues);
    final RM imagValue = realValues[0][0].createGrid(imagValues);

    final RM realVector = realValues[0][0].createGrid(eig.getReVector());
    final RM imagVector = realValues[0][0].createGrid(eig.getImVector());

    return new EigenSolution<>(realValue, imagValue, realVector, imagVector);
  }

  /**
   * {@inheritDoc}
   */
  public EigenSolution<RS,RM,CS,CM> eigenDecompose(final CM b) {
    throw new UnsupportedOperationException();
    
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//    if (hasSameRowSize(b) == false) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final EigenSolutionElements<RS,RM,CS,CM> eig = new ComplexGeneralizedEigenSolver<RS,RM,CS,CM>().solve(getElements(), b.getElements());
//    final RS[][] realValues = GridUtil.diagonal(eig.getReValue());
//    final RS[][] imagValues = GridUtil.diagonal(eig.getImValue());
//    final RM realValue = realValues[0][0].createGrid(realValues);
//    final RM imagValue = imagValues[0][0].createGrid(imagValues);
//    
//    final RS[][] realVectors = eig.getReVector();
//    final RS[][] imagVectors = eig.getImVector();
//    final RM realVector = realVectors[0][0].createGrid(realVectors);
//    final RM imagVector = imagVectors[0][0].createGrid(imagVectors);
//
//    return new EigenSolution<>(realValue, imagValue, realVector, imagVector);
  }

  
  /**
   * {@inheritDoc}
   */
  @Override
  public CM eigenValue() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final ComplexEigenSolver<RS,RM,CS, CM> eigenSolver = new ComplexEigenSolver<>();
    final CS[] values = eigenSolver.getEigenValue(getElements());
    final CS[][] eigenValues = GridUtil.transpose(values);
    return eigenValues[0][0].createGrid(eigenValues);
  }

  /**
   * {@inheritDoc}
   */
  public CM eigenValue(final CM b) {
    throw new UnsupportedOperationException();
    
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    }
//    if (getRowSize() != b.getRowSize()) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final ComplexGeneralizedEigenSolver<RS,RM,CS,CM> eigenSolver = new ComplexGeneralizedEigenSolver<>();
//    final CS[] values = eigenSolver.getEigenValue(getElements(), b.getElements());
//    
//    CS[][] eigenValues = GridUtil.transpose(values);
//    return eigenValues[0][0].createGrid(eigenValues);
  }

  
  /**
   * {@inheritDoc}
   */
  @Override
  public CM eigenVector() {
    if (isSquare() == false) {
      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
    }

    final ComplexEigenSolver<RS, RM,CS,CM> eigenSolver = new ComplexEigenSolver<>();
    final CS[][] vectors = eigenSolver.getEigenVector(getElements());
    return vectors[0][0].createGrid(vectors);
  }
  
  /**
   * {@inheritDoc}
   */
  public CM eigenVector(final CM b) {
    throw new UnsupportedOperationException();
    
//    if (isSquare() == false || b.isSquare() == false) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_SQUARE_MATRIX);
//    } 
//    if (getRowSize() != b.getRowSize()) {
//      throw new MatrixSizeException(this, b, MatrixSizeException.INCORRECT_SIZE);
//    }
//
//    final CS[][] vectors = new ComplexGeneralizedEigenSolver<RS,RM,CS,CM>().getEigenVector(getElements(), b.getElements());
//    return vectors[0][0].createGrid(vectors);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public SingularValueDecomposition<CS, CM> singularValueDecompose() {
    final SingularValueDecompositionElements<CS, CM> udv = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().decompose(getElements());

    CS[][] uu = udv.getU();
    CS[][] dd = udv.getD();
    CS[][] vv = udv.getV();
    final CM u = uu[0][0].createGrid(uu);
    final CM d = dd[0][0].createGrid(dd);
    final CM v = vv[0][0].createGrid(vv);
    return new SingularValueDecomposition<>(u, d, v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CM singularValue() {
    final CS[][] elements = getElements();
    final RS[] singularValues = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().singularValue(elements);
    final CS[] values = singularValues[0].createComplexArray(singularValues);
    return elements[0][0].createGrid(values).transpose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int rank(final CS tolerance) {
    return new ComplexSingularValueDecomposer<RS, RM,CS,CM>().rank(getElements(), tolerance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CM pseudoInverse(final CS tolerance) {
    final CS[][] pseudoInverse = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().pseudoInverse(getElements(), tolerance);
    return pseudoInverse[0][0].createGrid(pseudoInverse);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CS maxSingularValue() {
    final CS[][] elements = getElements();
    final RS maximumSingularValue = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().maximumSingularValue(elements);
    return elements[0][0].create(maximumSingularValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CS minSingularValue() {
    final CS[][] elements = getElements();
    final RS minimumSingularValue = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().minimumSingularValue(elements);
    return elements[0][0].create(minimumSingularValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CM kernel(final CS tolerance) {
    final CS[][] kernel = new ComplexSingularValueDecomposer<RS, RM,CS,CM>().kernel(getElements(), tolerance);
    return kernel[0][0].createGrid(kernel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFullRank(final CS tolerance) {
    return new ComplexSingularValueDecomposer<RS, RM,CS,CM>().isFullRank(getElements(), tolerance);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public boolean isTransformableFrom(final Matrix<?, ?> value) {
//    if (value instanceof BaseNumericalMatrix<?, ?> && (isEmpty() == false) && (value.isEmpty() || getElement(1, 1).isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
//      return true;
//    }
//    if (value instanceof IntMatrix) {
//      return true;
//    }
//    if (value instanceof DoubleMatrix) {
//      return true;
//    }
//    if (super.isTransformableFrom(value)) {
//      return true;
//    }
//
//    return false;
//  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public BaseNumericalComplexMatrix<S, M> transformFrom(final Matrix<?, ?> value) {
//    final int valueRowSize = value.getRowSize();
//    final int valueColumnSize = value.getColumnSize();
//
//    if (value instanceof BaseNumericalMatrix<?, ?> && (isEmpty() == false) && (value.isEmpty() || getElement(1, 1).isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
//      return new BaseNumericalComplexMatrix<>(valueRowSize, valueColumnSize, createArrayWithTransformationFrom(((BaseMatrix<?, ?>)value).getElements()));
//    }
//
//    if (value instanceof IntMatrix) {
//      return new BaseNumericalComplexMatrix<>(valueRowSize, valueColumnSize, createArray(((IntMatrix)value).getIntElements()));
//    }
//    if (value instanceof DoubleMatrix) {
//      return new BaseNumericalComplexMatrix<>(valueRowSize, valueColumnSize, createArray(((DoubleMatrix)value).getDoubleElements()));
//    }
//
//    if (super.isTransformableFrom(value)) {
//      return super.transformFrom(value);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.26") + value); //$NON-NLS-1$
//  }

//  /**
//   * int型の配列をE型の配列に変換します。
//   * 
//   * @param intElements int型の配列
//   * @return E型の配列に変換します。
//   */
//  private BaseComplexNumericalScalar<S, M>[][] createArray(final int[][] intElements) {
//    final int ansRowSize = intElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : intElements[0].length;
//    final BaseComplexNumericalScalar<S, M> value = getElement(1, 1);
//    final BaseComplexNumericalScalar<S, M>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = value.transformFrom(intElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

//  /**
//   * double型の配列をE型の配列に変換します。
//   * 
//   * @param doubleElements double型の配列
//   * @return E型の配列に変換します。
//   */
//  private BaseComplexNumericalScalar<S, M>[][] createArray(final double[][] doubleElements) {
//    final int ansRowSize = doubleElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : doubleElements[0].length;
//    final BaseComplexNumericalScalar<S, M> value = getElement(1, 1);
//    final BaseComplexNumericalScalar<S, M>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = value.transformFrom(doubleElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

//  /**
//   * MatrixElement型の配列をE型の配列に変換します。
//   * 
//   * @param matrixElements MatrixElement型の配列
//   * @return E型の配列に変換します。
//   */
//  private BaseComplexNumericalScalar<S, M>[][] createArrayWithTransformationFrom(final Scalar<?, ?>[][] matrixElements) {
//    final int ansRowSize = matrixElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : matrixElements[0].length;
//    final BaseComplexNumericalScalar<S, M> value = getElement(1, 1);
//    final BaseComplexNumericalScalar<S, M>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
//
//    for (int row = 0; row < ansRowSize; row++) {
//      for (int column = 0; column < ansColumnSize; column++) {
//        ansElements[row][column] = value.transformFrom(matrixElements[row][column]);
//      }
//    }
//
//    return ansElements;
//  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CM fftRowWise(final int dataSize) {
    //    if (dataSize - Math.pow(2, Math.log(dataSize) / Math.log(2)) != 0 || dataSize == 1) {
    //      throw new MatrixSizeException(Messages.getString("NumericalMatrix.13")); //$NON-NLS-1$
    //    }

    final RS scalar = getElement(1, 1).getRealPart();

    final RS[][] ansr = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] ansi = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] mr = getRealPartElements();
    final RS[][] mi = getImaginaryPartElements();

    for (int i = 0; i < getRowSize(); i++) {
      final RS[][] tmp = ComplexFFTAnalyzer.fft(mr[i], mi[i], dataSize);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }

    final CS[][] ansComplex =ansr[0][0].createComplexArray(ansr,ansi);
    return ansComplex[0][0].createGrid(getRowSize(), getColumnSize(), ansComplex);
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
  public CM fftColumnWise() {
    return fftColumnWise(getRowSize());
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
  public RM getRealPart() {
    final RS[][] ans = AbstractNumericalMatrixUtil.getRealPartElements(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 実部の成分を返します。
   * 
   * @return 実部の成分
   */
  protected RS[][] getRealPartElements() {
    final RS[][] ans = AbstractNumericalMatrixUtil.getRealPartElements(getElements());
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public RM getImaginaryPart() {
    final RS[][] ans = AbstractNumericalMatrixUtil.getImaginaryPartElements(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * 虚部の成分を返します。
   * 
   * @return 虚部の成分
   */
  protected RS[][] getImaginaryPartElements() {
    final RS[][] ans = AbstractNumericalMatrixUtil.getImaginaryPartElements(getElements());
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(IntMatrix realPart) {
    AbstractNumericalMatrixUtil.setRealPartElements(getElements(), realPart.getIntElements());
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(IntMatrix imaginaryPart) {
    AbstractNumericalMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getIntElements());
  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(final Matrix<?, ?> realPart) {
//    if (realPart instanceof IntMatrix) {
//      setRealPart((IntMatrix)realPart);
//      return;
//    }
//    if (realPart instanceof DoubleMatrix) {
//      setRealPart((DoubleMatrix)realPart);
//      return;
//    }
//    if (realPart instanceof BaseMatrix<?, ?>) {
//      setRealPart(realPart);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.24")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(final IntMatrix realPart) {
//    BaseNumericalMatrixUtil.setRealPartElements(getElements(), realPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(final DoubleMatrix realPart) {
//    BaseNumericalMatrixUtil.setRealPartElements(getElements(), realPart.getDoubleElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setRealPart(final BaseNumericalMatrix<S, ?> realPart) {
//    BaseNumericalMatrixUtil.setRealPartElements(getElements(), realPart.getElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(final Matrix<?, ?> imagPart) {
//    if (imagPart instanceof IntMatrix) {
//      setImaginaryPart((IntMatrix)imagPart);
//      return;
//    }
//    if (imagPart instanceof DoubleMatrix) {
//      setImaginaryPart((DoubleMatrix)imagPart);
//      return;
//    }
//    if (imagPart instanceof BaseMatrix<?, ?>) {
//      setImaginaryPart(imagPart);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.25")); //$NON-NLS-1$
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(final IntMatrix imaginaryPart) {
//    BaseNumericalMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(final DoubleMatrix imaginaryPart) {
//    BaseNumericalMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getDoubleElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public void setImaginaryPart(final BaseNumericalMatrix<S, ?> imaginaryPart) {
//    BaseNumericalMatrixUtil.setImagPartElements(getElements(), imaginaryPart.getElements());
//  }

  /**
   * 各成分の偏角を成分に持つ行列を返します。
   * 
   * @return 偏角行列
   */
  public RM argumentElementWise() {
    final RS[][] ans = AbstractNumericalComplexMatrixUtil.argumentElementWise(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
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
  @Override
  public CM ifftRowWise(final int dataSize) {
    final RS scalar = getElement(1, 1).getRealPart();
    final RS[][] ansr = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] ansi = scalar.createArray(getRowSize(), getColumnSize());
    final RS[][] mr = getRealPartElements();
    final RS[][] mi = getImaginaryPartElements();

    for (int i = 0; i < getRowSize(); i++) {
      final RS[][] tmp = ComplexFFTAnalyzer.ifft(mr[i], mi[i], dataSize);
      ansr[i] = tmp[0];
      ansi[i] = tmp[1];
    }

    final CS[][] ansComplex = ansr[0][0].createComplexArray(ansr, ansi);
    return ansComplex[0][0].createGrid(getRowSize(), getColumnSize(), ansComplex);
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
  @Override
  public CM ifft(final int dataSize) {
    if (getRowSize() == 1) {
      final RS[] re = getRealPartElements()[0];
      final RS[] im = getImaginaryPartElements()[0];
      final RS[][] ans = ComplexFFTAnalyzer.ifft(re, im, dataSize);
      final CS[] ansComplex = ans[0][0].createComplexArray(ans[0], ans[1]);
      return ansComplex[0].createGrid(ansComplex);
    }

    if (getColumnSize() == 1) {
      return this.transpose().ifft(dataSize).transpose();
    }

    throw new MatrixSizeException(Messages.getString("NumericalMatrix.16")); //$NON-NLS-1$
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
  public CM ifftColumnWise(final int dataSize) {
    return this.transpose().ifftRowWise(dataSize).transpose();
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
  @Override
  public CM fft(final int dataSize) {
    if (getRowSize() == 1) {
      final RS[] re = this.getRealPartElements()[0];
      final RS[] im = this.getImaginaryPartElements()[0];
      final RS[][] ans = ComplexFFTAnalyzer.fft(re, im, dataSize);
      final CS[] ansComplex = ans[0][0].createComplexArray(ans[0], ans[1]);
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
  @Override
  public CM absElementWise() {
    final CS[][] ans = AbstractNumericalMatrixUtil.absElementWise(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public CM sqrt() {
    return matrixFunction(new SqrtFunction());
  }
  
  /**
   * {@inheritDoc}
   */
  public ComplexQZDecomposition<RS,RM,CS,CM> qzDecompose(final CM b) {
    throw new UnsupportedOperationException();
    
//    final QZDecompositionElements<RS,RM> qz = new ComplexQZDecomposer<RS,RM>().decompose(getElements(), b.getElements());
//    RS[][] aaa = qz.getAA();
//    final RM aa =aaa[0][0].createGrid(aaa);
//    RS[][] bbb = qz.getBB();
//    final RM bb = bbb[0][0].createGrid(aaa);
//    RS[][] qqq = qz.getQ();
//    final RM q =qqq[0][0].createGrid(aaa);
//    RS[][] zzz = qz.getZ();
//    final  RM z = zzz[0][0].createGrid(aaa);
//    RS[][] xxxRe = qz.getReX();
//    final RM xReal = xxxRe[0][0].createGrid(aaa);
//    RS[][] xxxIm = qz.getImX();
//    final RM xImag = xxxIm[0][0].createGrid(aaa);
//    final CM x = xReal.createComplex(xReal, xImag);
//    return new ComplexQZDecomposition<>(aa, bb, q, z, x);
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(RM realPart) {
    CS[][] elements =  getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setRealPart(realPart.getElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(RM imaginaryPart) {
    CS[][] elements =  getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setImaginaryPart(imaginaryPart.getElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public CM create(RM rePart) {
    return rePart.toComplex();
  }

  /**
   * {@inheritDoc}
   */
  public CM create(RM rePart, RM imPart) {
    return rePart.toComplex().create(rePart, imPart);
  }
    
  /**
   * {@inheritDoc}
   */
  public CM add(RM value) {
    return add(value.toComplex());
  }
  
  /**
   * {@inheritDoc}
   */
  public CM subtract(RM value) {
    return subtract(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM multiply(RM value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(RM value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(RM value) {
    return leftDivide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM appendDown(RM value) {
    return appendDown(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM appendRight(RM value) {
    return appendRight(value.toComplex());
  }
  
  /**
   * {@inheritDoc}
   */
  public CM multiply(RS value) {
    return multiply(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM divide(RS value) {
    return divide(value.toComplex());
  }

  /**
   * {@inheritDoc}
   */
  public CM leftDivide(RS value) {
    return leftDivide(value.toComplex());
  }
}
