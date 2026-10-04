/*
 * $Id: SymbolicMatrix.java,v 1.27 2008/07/16 04:58:02 koga Exp $
 *
 * Copyright (C) 2004-2005 Koga Laboratory. All rights reserved.
 */

package org.mklab.nfc.matrix;

import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.SymbolicScalar;


/**
 * {@link SymbolicScalar}を成分とする行列を表すクラスです。
 * 
 * @author koga
 * @version $Revision: 1.27 $
 * @param <M> 行列の型
 * @param <S> スカラーの型
 * @param <ES> 係数スカラーの型
 * @param <EM> 係数行列の型
 */
public abstract class AbstractSymbolicMatrix<S extends SymbolicScalar<S,M,ES,EM>, M extends SymbolicMatrix<S,M,ES,EM>, ES extends NumericalScalar<ES,EM>, EM extends NumericalMatrix<ES,EM>> extends BaseMatrix<S,M> implements SymbolicMatrix<S,M,ES,EM> {

  /** シリアル番号。 */
  private static final long serialVersionUID = 8453169358682122975L;
  
  /** 成分の出力フォーマット 。*/
  //private static String defaultElementFormat = "%16.8E"; //$NON-NLS-1$
  private static String defaultElementFormat = "%15G"; //$NON-NLS-1$
  
  /**
   * Creates {@link AbstractSymbolicMatrix}.
   * @param matrix 行列
   */
  public AbstractSymbolicMatrix(M matrix) {
    super(matrix);
  }
  
  /**
   * 新しく生成された<code>BaseNumericalMatrix</code>オブジェクトを初期化します。
   * @param elements 成分
   */
  public AbstractSymbolicMatrix(final S[] elements) {
    super(elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

  /**
   * elementsで与えられた成分を持つ数値行列を生成します。
   * 
   * @param elements 成分
   */
  public AbstractSymbolicMatrix(final S[][] elements) {
    super(elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

  /**
   * elementsで与えられた成分をもつrowSize*columnSizeの数式行列を生成します。
   * 
   * @param rowSize 行の数
   * @param columnSize 列の数
   * @param elements 成分
   */
  public AbstractSymbolicMatrix(final int rowSize, final int columnSize, final S[][] elements) {
    super(rowSize, columnSize, elements);
    setElementFormat(defaultElementFormat);
    setElementAlignment(GridElementAlignment.LEFT);
  }

  /**
   * {@inheritDoc}
   */
  public M derivative() {
    final M ans = derivative(1);
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public M derivative(final int order) {
    final S[][] ans = AbstractSymbolicMatrixUtil.derivative(getElements(), order);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M shiftLower() {
    return shiftLower(1);
  }

  /**
   * {@inheritDoc}
   */
  public M shiftLower(final int count) {
    final S[][] ans = AbstractSymbolicMatrixUtil.shiftLower(getElements(), count);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M shiftHigher() {
    return shiftHigher(1);
  }

  /**
   * {@inheritDoc}
   */
  public M shiftHigher(final int count) {
    final S[][] ans = AbstractSymbolicMatrixUtil.shiftHigher(getElements(), count);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public void setVariable(final String variableName) {
    AbstractSymbolicMatrixUtil.setVariable(getElements(), variableName);
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final int value) {
    final ES[][] ans = AbstractSymbolicMatrixUtil.evaluate(getElements(), value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final double value) {
    final ES[][] ans = AbstractSymbolicMatrixUtil.evaluate(getElements(), value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final ES value) {
    final ES[][] ans = AbstractSymbolicMatrixUtil.evaluate(getElements(), value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public M evaluate(final S value) {
    final S[][] ans = AbstractSymbolicMatrixUtil.evaluate(getElements(), value);
    return ans[0][0].createGrid(getRowSize(), getColumnSize(), ans);
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final IntMatrix value) {
    final int argRowSize = value.getRowSize();
    final int argColumnSize = value.getColumnSize();

    final EM unit = evaluate(value.getIntElement(1, 1));
    final EM ans = unit.createZero(getRowSize() * argRowSize, getColumnSize() * argColumnSize);

    for (int row = 1; row <= argRowSize; row++) {
      for (int column = 1; column <= argColumnSize; column++) {
        ans.setSubMatrix(row, column, this, evaluate(value.getIntElement(row, column)));
      }
    }

    return ans;
  }

//  /**
//   * {@inheritDoc}
//   */
//  public NumericalMatrix<?,?> evaluate(final DoubleMatrix value) {
//    final int argRowSize = value.getRowSize();
//    final int argColumnSize = value.getColumnSize();
//
//    final NumericalMatrix<?,?> unit = evaluate(value.getDoubleElement(1, 1));
//    final NumericalMatrix<?,?> ans = unit.createZero(getRowSize() * argRowSize, getColumnSize() * argColumnSize);
//
//    for (int row = 1; row <= argRowSize; row++) {
//      for (int column = 1; column <= argColumnSize; column++) {
//        ans.setSubMatrix(row, column, this, evaluate(value.getDoubleElement(row, column)));
//      }
//    }
//
//    return ans;
//  }

  /**
   * {@inheritDoc}
   */
  public EM evaluate(final EM value) {
    final int argRowSize = value.getRowSize();
    final int argColumnSize = value.getColumnSize();

    final EM unit = evaluate(value.getElement(1, 1));
    final EM ans = unit.createZero(getRowSize() * argRowSize, getColumnSize() * argColumnSize);

    for (int row = 1; row <= argRowSize; row++) {
      for (int column = 1; column <= argColumnSize; column++) {
        ans.setSubMatrix(row, column, this, evaluate(value.getElement(row, column)));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public EM evaluateElementWise(final EM matrix) {
    final int argRowSize = matrix.getRowSize();
    final int argColumnSize = matrix.getColumnSize();

    final S[][] elements = getElements();
    final EM unit = elements[0][0].evaluate(matrix);
    final EM ans = unit.createZero(getRowSize() * argRowSize, getColumnSize() * argColumnSize);

    for (int row = 0; row < getRowSize(); row++) {
      for (int column = 0; column < getColumnSize(); column++) {
        final S equation = elements[row][column];
        ans.setSubMatrix(row+1, column+1, matrix, equation.evaluate(matrix));
      }
    }

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public M powerElementWise(M order) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public M multiply(final ES value) {
    final S[][] elements = getElements();
    final M ans = createZero();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans.setElement(i+1, j+1, elements[i][j].multiply(value));
      }
    }
    
    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public M divide(final ES value) {
    final S[][] elements = getElements();
    final M ans = createZero();

    for (int i = 0; i < getRowSize(); i++) {
      for (int j = 0; j < getColumnSize(); j++) {
        ans.setElement(i+1, j+1, elements[i][j].divide(value));
      }
    }
    
    return ans;
  }

}
