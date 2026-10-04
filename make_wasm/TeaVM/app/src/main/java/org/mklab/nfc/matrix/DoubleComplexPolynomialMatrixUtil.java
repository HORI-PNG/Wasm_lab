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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.util.EndianTransformer;

/**
 * Utility class of {@link DoubleComplexPolynomialMatrix}.
 * 
 * @author koga
 * @version $Revision$, 2021/08/12
 */
public class DoubleComplexPolynomialMatrixUtil {

  /**
   * Creates {@link DoubleComplexPolynomialMatrixUtil}.
   */
  private DoubleComplexPolynomialMatrixUtil() {
    // nothing to do
  }

  /**
   * 倍精度複素行列を多項式行列(元の行列を定数項とする)に変換します。
   * 
   * @param matrix 倍精度複素行列
   * @return 多項式行列
   */
  public static DoubleComplexPolynomial[][] createArray(final DoubleComplexMatrix matrix) {
    return DoubleComplexPolynomialMatrixUtil.createArray(matrix.getElements());
  }

  /**
   * 倍精度複素行列を多項式行列(元の行列を定数項とする)に変換します。
   * 
   * @param matrix 倍精度複素行列
   * @return 多項式行列
   */
  public static DoubleComplexPolynomial[] createArray(final DoubleComplexNumber[] matrix) {
    final int size = matrix.length;
  
    final DoubleComplexPolynomial[] ans = new DoubleComplexPolynomial[size];
    for (int i = 0; i < size; i++) {
      ans[i] = new DoubleComplexPolynomial(matrix[i]);
    }
    return ans;
  }

  /**
   * 倍精度複素行列を多項式行列(元の行列を定数項とする)に変換します。
   * 
   * @param matrix 倍精度複素行列
   * @return 多項式行列
   */
  public static DoubleComplexPolynomial[][] createArray(final DoubleComplexNumber[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  
    final DoubleComplexPolynomial[][] ans = new DoubleComplexPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final DoubleComplexNumber[] matrixi = matrix[i];
      final DoubleComplexPolynomial[] ansi = ans[i];
      for (int j = 0; j < columnSize; j++) {
        ansi[j] = new DoubleComplexPolynomial(matrixi[j]);
      }
    }
    return ans;
  }

  /**
   * 行列を出力ストリームに出力(MXフォーマット)します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final DoubleComplexPolynomial[][] matrix, final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(matrix, name);
  
    head.write(output);
  
    final int rowSize = matrix.length;
    final int columnSize = matrix.length == 0 ? 0 : matrix[0].length;
  
    if (head.getMatrixType() == MxDataHead.REAL_POLYNOMIAL_MATRIX) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt((matrix[i][j]).getDegree());
          (matrix[i][j]).writeMxFormatWithoutHeader(output, false);
        }
      }
    } else {
      // assert head.getMatrixType() == mxDataHead.COMPLEX_POLYNOMIAL_MATRIX;
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt((matrix[i][j]).getDegree());
          (matrix[i][j]).writeMxFormatWithoutHeader(output, true);
          // 全成分を複素多項式として保存
        }
      }
    }
  
    output.flush();
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 成分の出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final DoubleComplexPolynomial[][] matrix, final String elementFormat) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  
    final StringBuffer sb = new StringBuffer();
    final String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
  
    if (rowSize == 0 || columnSize == 0) {
      return "[[]]"; //$NON-NLS-1$
    }
  
    if (columnSize == 1 && rowSize != 1) {
      return toMmString(GridUtil.transpose(matrix), elementFormat) + "'"; //$NON-NLS-1$
    }
  
    if (rowSize != 1) {
      sb.append("["); //$NON-NLS-1$
    }
  
    final int displayColumnSize = 1;
  
    for (int i = 0; i < rowSize; i++) {
      if (i != 0) {
        sb.append(" "); //$NON-NLS-1$
      }
  
      for (int k = 0; k < columnSize;) {
        if (k == 0) {
          sb.append("["); //$NON-NLS-1$
        } else {
          sb.append(" "); //$NON-NLS-1$ 
          if (rowSize != 1) {
            sb.append(" "); //$NON-NLS-1$ 
          }
        }
  
        int j;
        for (j = k; j < k + displayColumnSize && j < columnSize; j++) {
          sb.append((matrix[i][j]).toMmString(elementFormat));
          if (j != columnSize - 1) {
            sb.append(","); //$NON-NLS-1$
          }
        }
  
        if (j == columnSize) {
          sb.append("]"); //$NON-NLS-1$
          if (i != rowSize - 1) {
            sb.append(newLine);
          }
        } else {
          sb.append(newLine);
        }
  
        k += displayColumnSize;
      }
    }
  
    if (rowSize != 1) {
      sb.append("]"); //$NON-NLS-1$
    }
  
    return sb.toString();
  }

  /**
   * 入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static DoubleComplexPolynomial[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final int rowSize = head.getRowSize();
    final int columnSize = head.getColumnSize();
    final int matrixType = head.getMatrixType();
    //final int version = head.getVersion();
  
    final DoubleComplexPolynomial[][] ans = new DoubleComplexPolynomial[rowSize][columnSize];
  
    final DataInputStream is = new DataInputStream(input);
  
    if (matrixType == MxDataHead.REAL_POLYNOMIAL_MATRIX) {
      throw new UnsupportedOperationException();
    }
    
    // ComplexPolynomialMatrix
    // assert matrixType == 3;
    if (head.isSameEndian()) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          final int degree = is.readInt();
          final DoubleComplexNumber[] coef = new DoubleComplexNumber[degree + 1];
          for (int k = 0; k <= degree; k++) {
            final double real = is.readDouble();
            final double imag = is.readDouble();
            coef[k] = new DoubleComplexNumber(real, imag);
          }
          ans[i][j] = new DoubleComplexPolynomial(coef);
        }
      }
    } else {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          final int degree = EndianTransformer.flip(is.readInt());
          final DoubleComplexNumber[] coef = new DoubleComplexNumber[degree + 1];
          for (int k = 0; k <= degree; k++) {
            final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            coef[k] = new DoubleComplexNumber(real, imag);
          }
          ans[i][j] = new DoubleComplexPolynomial(coef);
        }
      }
    }
  
    return ans;
  }

}
