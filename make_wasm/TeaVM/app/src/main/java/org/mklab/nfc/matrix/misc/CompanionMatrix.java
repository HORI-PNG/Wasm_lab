/*
 * Created on 2006/07/25
 * Copyright (C) 2006 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.matrix.misc;

import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.scalar.NumericalScalar;


/**
 * コンパニオン行列を生成するクラスです。
 * 
 * @author koga
 * @version $Revision$, 2006/07/25
 */
public final class CompanionMatrix {

  /**
   * 新しく生成された<code>CompanionMatrix</code>オブジェクトを初期化します。
   */
  private CompanionMatrix() {
    // nothing to do
  }

//  /**
//   * 特性多項式の係数から求めたコンパニオン行列(同判形)を返します。
//   * 
//   * <p>例えば以下の特性多項式
//   * 
//   * <blockquote>
//   * 
//   * s <sup>n </sup>+ a <sub>n-1 </sub>s <sup>n-1 </sup>+ a <sub>n-2 </sub>s <sup>n-2 </sup>+・ ・ ・+a <sub>1 </sub>s + a <sub>0 </sub>
//   * 
//   * </blockquote>
//   * 
//   * を考えたとき、コンパニオン行列は、
//   * 
//   * <blockquote>
//   * 
//   * a <sub>0 </sub>, a<sub>1 </sub>,・・・,a <sub>n-1 </sub>
//   * 
//   * </blockquote>
//   * 
//   * を成分に持つ行列に 対して、このメソッドを実行することで得られます。
//   * 
//   * @param <S> スカラーの型
//   * @param <M> 行列の型
//   * @param coefficientVector 特性多項式の係数を成分とするベクトル
//   * @return コンパニオン行列
//   */
//  public static <S extends Scalar<S, M>, M extends Matrix<S, M>> M create(final M coefficientVector) {
//    final int rowSize = coefficientVector.getRowSize();
//    final int columnSize = coefficientVector.getColumnSize();
//
//    if (rowSize != 1 && columnSize != 1) {
//      throw new MatrixSizeException(MatrixSizeException.NOT_A_VECTOR_MATRIX);
//    }
//
//    final M vector = rowSize == 1 ? coefficientVector : coefficientVector.transpose();
//
//    if (coefficientVector instanceof DoubleMatrix) {
//      return (M)CompanionMatrix.create((DoubleMatrix)vector);
//    }
//
//    if (coefficientVector instanceof BaseMatrix<?, ?>) {
//      return (M)CompanionMatrix.create((BaseMatrix<?, ?>)vector);
//    }
//
//    throw new IllegalArgumentException(Messages.getString("CompanionMatrix.0")); //$NON-NLS-1$
//  }

  /**
   * 特性多項式の係数から求めたコンパニオン行列(同判形)を返します。
   * 
   * <p>例えば以下の特性多項式
   * 
   * <blockquote>
   * 
   * s <sup>n </sup>+ a <sub>n-1 </sub>s <sup>n-1 </sup>+ a <sub>n-2 </sub>s <sup>n-2 </sup>+・ ・ ・+a <sub>1 </sub>s + a <sub>0 </sub>
   * 
   * </blockquote>
   * 
   * を考えたとき、コンパニオン行列は、
   * 
   * <blockquote>
   * 
   * a <sub>0 </sub>, a<sub>1 </sub>,・・・,a <sub>n-1 </sub>
   * 
   * </blockquote>
   * 
   * を成分に持つ行列に 対して、このメソッドを実行することで得られます。
   * 
   * @param coefficientVector 特性多項式の係数を成分とするベクトル
   * @return コンパニオン行列
   */
  public static DoubleMatrix create(final DoubleMatrix coefficientVector) {
    final int size = coefficientVector.getColumnSize();
    final DoubleMatrix ans = new DoubleMatrix(size, size);

    for (int i = 1; i < size; i++) {
      ans.setElement(i, i + 1, 1);
      ans.setElement(size, i, -coefficientVector.getDoubleElement(1, i));
    }
    ans.setElement(size, size, -coefficientVector.getDoubleElement(1, size));

    return ans;
  }

  /**
   * 特性多項式の係数から求めたコンパニオン行列(同判形)を返します。
   * 
   * <p>例えば以下の特性多項式
   * 
   * <blockquote>
   * 
   * s <sup>n </sup>+ a <sub>n-1 </sub>s <sup>n-1 </sup>+ a <sub>n-2 </sub>s <sup>n-2 </sup>+・ ・ ・+a <sub>1 </sub>s + a <sub>0 </sub>
   * 
   * </blockquote>
   * 
   * を考えたとき、コンパニオン行列は、
   * 
   * <blockquote>
   * 
   * a <sub>0 </sub>, a<sub>1 </sub>,・・・,a <sub>n-1 </sub>
   * 
   * </blockquote>
   * 
   * を成分に持つ行列に 対して、このメソッドを実行することで得られます。
   * 
   * @param coefficientVector 特性多項式の係数を成分とするベクトル
   * @return コンパニオン行列
   */
  public static IntMatrix create(final IntMatrix coefficientVector) {
    final int size = coefficientVector.getColumnSize();
    final IntMatrix ans = new IntMatrix(size, size);

    for (int i = 1; i < size; i++) {
      ans.setElement(i, i + 1, 1);
      ans.setElement(size, i, -coefficientVector.getIntElement(1, i));
    }
    ans.setElement(size, size, -coefficientVector.getIntElement(1, size));

    return ans;
  }
  
  /**
   * 特性多項式の係数から求めたコンパニオン行列(同判形)を返します。
   * 
   * <p>例えば以下の特性多項式
   * 
   * <blockquote>
   * 
   * s <sup>n </sup>+ a <sub>n-1 </sub>s <sup>n-1 </sup>+ a <sub>n-2 </sub>s <sup>n-2 </sup>+・ ・ ・+a <sub>1 </sub>s + a <sub>0 </sub>
   * 
   * </blockquote>
   * 
   * を考えたとき、コンパニオン行列は、
   * 
   * <blockquote>
   * 
   * a <sub>0 </sub>,a <sub>1 </sub>,・・・a <sub>n-1 </sub>
   * 
   * </blockquote>
   * 
   * を成分に持つ行列に 対して、このメソッドを実行することで得られます。
   * 
   * @param <M> 行列の型
   * @param <S> 成分の型
   * 
   * @param coefficientVector 特性多項式の係数を成分とするベクトル
   * @return コンパニオン行列
   */
  public static <S extends NumericalScalar<S, M>, M extends NumericalMatrix<S, M>> M create(final M coefficientVector) {
    final int size = coefficientVector.getColumnSize();
    final M ans = coefficientVector.createZero(size, size);
    final S unit = coefficientVector.getElement(1, 1).createUnit();

    for (int i = 1; i < size; i++) {
      ans.setElement(i, i + 1, unit.clone());
      ans.setElement(size, i, coefficientVector.getElement(1, i).unaryMinus());
    }
    ans.setElement(size, size, coefficientVector.getElement(1, size).unaryMinus());

    return ans;
  }
}
