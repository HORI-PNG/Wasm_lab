/*
 * Created on 2008/03/16
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.AbstractComplexSymbolicScalar;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.ComplexSymbolicScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;
import org.mklab.nfc.scalar.RealSymbolicScalar;


/**
 * {@link AbstractComplexSymbolicScalar}を成分とする行列を表わすクラスです。
 * 
 * @author koga
 * @version $Revision: 1.7 $, 2008/03/16
 * @param <RM> 実行列の型
 * @param <RS> 実スカラーの型
 *  @param <CS> 複素スカラーの型
 * @param <CM> 複素行列の型
 * @param <RES> 実係数スカラーの型
 * @param <REM> 実係数行列の型
 * @param <CES> 複素係数スカラーの型
 * @param <CEM> 複素係数行列の型
 */
public class AbstractSymbolicComplexMatrix<RS extends RealSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, RM extends RealSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>,  CS extends ComplexSymbolicScalar<RS,RM,CS,CM,RES,REM,CES,CEM>, CM extends ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>, RES extends RealNumericalScalar<RES,REM,CES,CEM>, REM extends RealNumericalMatrix<RES,REM,CES,CEM>,  CES extends ComplexNumericalScalar<RES,REM,CES,CEM>, CEM extends ComplexNumericalMatrix<RES,REM,CES,CEM>> extends AbstractSymbolicMatrix<CS,CM,CES,CEM>  implements ComplexSymbolicMatrix<RS,RM,CS,CM,RES,REM,CES,CEM>{

  /** シリアル番号。 */
  private static final long serialVersionUID = 2919182055081619403L;

  /**
   * 
   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   */
  public AbstractSymbolicComplexMatrix(final CS[] elements) {
    super(elements);
  }

//  /**
//   * 
//   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   * @param imagElements 虚部セリ分
//   */
//  public BaseSymbolicComplexMatrix(final RS[] realElements, final RS[] imagElements) {
//    this(BaseSymbolicMatrixUtil.createComplexArray(realElements, imagElements));
//  }
//
//  /**
//   * 
//   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   */
//  public BaseSymbolicComplexMatrix(final RS[] realElements) {
//    this(BaseSymbolicMatrixUtil.createComplexArray(realElements));
//  }

//  /**
//   * 
//   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   * @param imagElements 虚部セリ分
//   */
//  public BaseSymbolicComplexMatrix(final RM realElements, final RM imagElements) {
//    this(realElements.getElements(), imagElements.getElements());
//  }

  /**
   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param elements 成分
   */
  public AbstractSymbolicComplexMatrix(final CS[][] elements) {
    super(elements);
  }
  
//  /**
//   * 
//   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   * @param imagElements 虚部セリ分
//   */
//  public BaseSymbolicComplexMatrix(final RS[][] realElements, final RS[][] imagElements) {
//    this(BaseSymbolicMatrixUtil.createComplexArray(realElements, imagElements));
//  }
//
//  /**
//   * 
//   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
//   * 
//   * @param realElements 実部成分
//   */
//  public BaseSymbolicComplexMatrix(final RS[][] realElements) {
//    this(BaseSymbolicMatrixUtil.createComplexArray(realElements));
//  }


  /**
   * 新しく生成された<code>SymbolicComplexMatrix</code>オブジェクトを初期化します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分
   */
  public AbstractSymbolicComplexMatrix(final int rowSize, final int columnSize, final CS[][] elements) {
    super(rowSize, columnSize, elements);
  }

//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  public final boolean isTransformableFrom(final RMatrix<?,?> value) {
//    if (value instanceof BaseSymbolicMatrix && (isEmpty() == false) && (value.isEmpty() || getElement(1, 1).isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
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
//  public final BaseSymbolicComplexMatrix<S,M,CS,CM> transformFrom(final RMatrix<?,?> value) {
//    final int valueRowSize = value.getRowSize();
//    final int valueColumnSize = value.getColumnSize();
//
//    if (value instanceof BaseSymbolicMatrix && (isEmpty() == false) && (value.isEmpty() || getElement(1, 1).isTransformableFrom(((BaseMatrix<?, ?>)value).getElement(1, 1)))) {
//      return new BaseSymbolicComplexMatrix<>(valueRowSize, valueColumnSize, createArrayWithTransformationFrom(((BaseMatrix<?, ?>)value).getElements()));
//    }
//
//    if (value instanceof IntMatrix) {
//      return new BaseSymbolicComplexMatrix<>(valueRowSize, valueColumnSize, createArray(((IntMatrix)value).getIntElements()));
//    }
//    if (value instanceof DoubleMatrix) {
//      return new BaseSymbolicComplexMatrix<>(valueRowSize, valueColumnSize, createArray(((DoubleMatrix)value).getDoubleElements()));
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
//  private BaseComplexSymbolicScalar<S,M,CS,CM>[][] createArray(final int[][] intElements) {
//    final int ansRowSize = intElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : intElements[0].length;
//    final BaseComplexSymbolicScalar<S,M,CS,CM> value = getElement(1, 1);
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
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
//  private BaseComplexSymbolicScalar<S,M,CS,CM>[][] createArray(final double[][] doubleElements) {
//    final int ansRowSize = doubleElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : doubleElements[0].length;
//    final BaseComplexSymbolicScalar<S,M,CS,CM> value = getElement(1, 1);
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
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
//  private BaseComplexSymbolicScalar<S,M,CS,CM>[][] createArrayWithTransformationFrom(final Scalar<?,?>[][] matrixElements) {
//    final int ansRowSize = matrixElements.length;
//    final int ansColumnSize = ansRowSize == 0 ? 0 : matrixElements[0].length;
//    final BaseComplexSymbolicScalar<S,M,CS,CM> value = getElement(1, 1);
//    final BaseComplexSymbolicScalar<S,M,CS,CM>[][] ansElements = value.createArray(ansRowSize, ansColumnSize);
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
   * Returns real part.
   * 
   * @return real part
   */
  public RM getRealPart() {
    final RS[][] ans = AbstractSymbolicMatrixUtil.getRealPartElements(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }
  
//  /**
//   * 実部の成分を返します。
//   * 
//   * @return 実部の成分
//   */
//  protected RS[][] getRealPartElements() {
//    final RS[][] ans = BaseSymbolicMatrixUtil.getRealPartElements(getElements());
//    return ans;
//  }

  /**
   * Returns imaginary part.
   * 
   * @return imaginary part
   */
  public RM getImaginaryPart() {
    final RS[][] ans = AbstractSymbolicMatrixUtil.getImaginaryPartElements(getElements());
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(RM realPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setRealPart(realPart.getElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(IntMatrix realPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setRealPart(realPart.getIntElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setRealPart(DoubleMatrix realPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setRealPart(realPart.getDoubleElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(RM imaginaryPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setImaginaryPart(imaginaryPart.getElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(DoubleMatrix imagPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setImaginaryPart(imagPart.getDoubleElement(i+1, j+1));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setImaginaryPart(IntMatrix imaginaryPart) {
    CS[][] elements = getElements();
    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        elements[i][j].setImaginaryPart(imaginaryPart.getIntElement(i+1, j+1));
      }
    }
  }
  
//  /**
//   * 虚部の成分を返します。
//   * 
//   * @return 虚部の成分
//   */
//  protected RS[][] getImaginaryPartElements() {
//    final RS[][] ans = BaseSymbolicMatrixUtil.getImaginaryPartElements(getElements());
//    return ans;
//  }


//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final IntMatrix realPart) {
//    BaseSymbolicMatrixUtil.setRealPartElements(getElements(), realPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setRealPart(final DoubleMatrix realPart) {
//    BaseSymbolicMatrixUtil.setRealPartElements(getElements(), realPart.getDoubleElements());
//  }

//  /**
//   * Sets real part.
//   * 
//   * @param realPart real part
//   */
//  public final void setRealPart(final RM realPart) {
//    if (realPart instanceof BaseSymbolicMatrix<?, ?,?,?>) {
//      setRealPart((BaseSymbolicMatrix<S,?,?,?>)realPart);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.24")); //$NON-NLS-1$
//  }

//  /**
//   * Sets real part
//   * 
//   * @param realPart real part
//   */
//  public final void setRealPart(final RM realPart) {
//    BaseSymbolicMatrixUtil.setRealPartElements(getElements(), realPart.getElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final IntMatrix imaginaryPart) {
//    BaseSymbolicMatrixUtil.setImaginaryPartElements(getElements(), imaginaryPart.getIntElements());
//  }

//  /**
//   * {@inheritDoc}
//   */
//  public final void setImaginaryPart(final DoubleMatrix imaginaryPart) {
//    BaseSymbolicMatrixUtil.setImaginaryPartElements(getElements(), imaginaryPart.getDoubleElements());
//  }

//  /**
//   * Sets imaginary part
//   * 
//   * @param imagPart imaginary part
//   */
//  public final void setImaginaryPart(final RM imagPart) {
//    if (imagPart instanceof BaseSymbolicMatrix<?, ?,?,?>) {
//      setImaginaryPart((BaseSymbolicMatrix<S,?,?,?>)imagPart);
//      return;
//    }
//
//    throw new IllegalArgumentException(Messages.getString("BaseMatrix.25")); //$NON-NLS-1$
//  }

//  /**
//   * Sets imaginary part.
//   * 
//   * @param imaginaryPart imaginary part
//   */
//  public final void setImaginaryPart(final RM imaginaryPart) {
//    BaseSymbolicMatrixUtil.setImaginaryPartElements(getElements(), imaginaryPart.getElements());
//  }

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
