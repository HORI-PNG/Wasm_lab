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
import java.nio.channels.UnsupportedAddressTypeException;

import org.mklab.nfc.matx.MxDataHead;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleComplexPolynomial;
import org.mklab.nfc.scalar.DoubleComplexRationalPolynomial;
import org.mklab.nfc.util.EndianTransformer;

/**
 * Utility class of {@link DoubleComplexRationalPolynomialMatrix}.
 * 
 * @author koga
 * @version $Revision$, 2021/08/12
 */
public class DoubleComplexRationalPolynomialMatrixUtil {

  /**
   * Creates {@link DoubleComplexRationalPolynomialMatrixUtil}.
   */
  private DoubleComplexRationalPolynomialMatrixUtil() {
    // nothing to do
  }

  /**
   * 倍精度有理多項式行列を有理多項式行列に変換します。
   * 
   * @param matrix 倍精度有理多項式行列
   * @return 有理多項式行列
   */
  public static DoubleComplexRationalPolynomial[] createArray(final DoubleComplexPolynomial[] matrix) {
    final int rowSize = matrix.length;
  
    final DoubleComplexRationalPolynomial[] ans = new DoubleComplexRationalPolynomial[rowSize];
  
    for (int i = 0; i < rowSize; i++) {
      final DoubleComplexPolynomial num = new DoubleComplexPolynomial(matrix[i].getCoefficients());
      final DoubleComplexPolynomial den = new DoubleComplexPolynomial(1);
      ans[i] = new DoubleComplexRationalPolynomial(num, den);
    }
    return ans;
  }

  /**
   * 倍精度有理多項式行列を有理多項式行列に変換します。
   * 
   * @param matrix 倍精度有理多項式行列
   * @return 有理多項式行列
   */
  public static DoubleComplexRationalPolynomial[][] createArray(final DoubleComplexPolynomial[][] matrix) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
  
    final DoubleComplexRationalPolynomial[][] ans = new DoubleComplexRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      final DoubleComplexRationalPolynomial[] ansi = ans[i];
      final DoubleComplexPolynomial[] matrixi = matrix[i];
      for (int j = 0; j < columnSize; j++) {
        final DoubleComplexPolynomial num = new DoubleComplexPolynomial(matrixi[j].getCoefficients());
        final DoubleComplexPolynomial den = new DoubleComplexPolynomial(1);
        ansi[j] = new DoubleComplexRationalPolynomial(num, den);
      }
    }
    return ans;
  }

  /**
   * 入力ストリームから行列データ(MXフォーマット)を読み込みます。
   * 
   * @param input 入力ストリーム
   * @param head MXフォーマットのヘッダ情報
   * @return 読み込んだ行列
   * @throws IOException 入力ストリームから読み込めない場合
   */
  public static DoubleComplexRationalPolynomial[][] readMxFormat(final InputStream input, final MxDataHead head) throws IOException {
    final int rowSize = head.getRowSize();
    final int columnSize = head.getColumnSize();
    final int matrixType = head.getMatrixType();
    //final int version = head.getVersion();
  
    final DoubleComplexRationalPolynomial[][] ans = new DoubleComplexRationalPolynomial[rowSize][columnSize];
  
    final DataInputStream is = new DataInputStream(input);
  
    if (matrixType == MxDataHead.REAL_RATIONAL_POLYNOMIAL_MATRIX) {
      throw new UnsupportedAddressTypeException();
      //      if (head.isSameEndian()) {
      //        for (int i = 0; i < rowSize; i++) {
      //          for (int j = 0; j < columnSize; j++) {
      //            final int nDegree = is.readInt();
      //            final int dDegree = is.readInt();
      //            final double[] nCoef = new double[nDegree + 1];
      //            for (int k = 0; k <= nDegree; k++) {
      //              nCoef[k] = is.readDouble();
      //            }
      //            final double[] dCoef = new double[dDegree + 1];
      //            for (int k = 0; k <= dDegree; k++) {
      //              dCoef[k] = is.readDouble();
      //            }
      //
      //            ans[i][j] = new RationalPolynomial(new Polynomial(nCoef, "s"), new Polynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
      //          }
      //        }
      //      } else {
      //        for (int i = 0; i < rowSize; i++) {
      //          for (int j = 0; j < columnSize; j++) {
      //            final int nDegree = EndianTransformer.flip(is.readInt());
      //            final int dDegree = EndianTransformer.flip(is.readInt());
      //            final double[] nCoef = new double[nDegree + 1];
      //            for (int k = 0; k <= nDegree; k++) {
      //              nCoef[k] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
      //            }
      //            final double[] dCoef = new double[dDegree + 1];
      //            for (int k = 0; k <= dDegree; k++) {
      //              dCoef[k] = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
      //            }
      //            ans[i][j] = new RationalPolynomial(new Polynomial(nCoef, "s"), new Polynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
      //          }
      //        }
      //      }
    }
    // ComplexRationalPolynomialMatrix
    // assert matrixType == 3;
    if (head.isSameEndian()) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          final int nDegree = is.readInt();
          final int dDegree = is.readInt();
          final DoubleComplexNumber[] nCoef = new DoubleComplexNumber[nDegree + 1];
          for (int k = 0; k <= nDegree; k++) {
            final double real = is.readDouble();
            final double imag = is.readDouble();
            nCoef[k] = new DoubleComplexNumber(real, imag);
          }
          final DoubleComplexNumber[] dCoef = new DoubleComplexNumber[dDegree + 1];
          for (int k = 0; k <= dDegree; k++) {
            final double real = is.readDouble();
            final double imag = is.readDouble();
            dCoef[k] = new DoubleComplexNumber(real, imag);
          }
          ans[i][j] = new DoubleComplexRationalPolynomial(new DoubleComplexPolynomial(nCoef, "s"), new DoubleComplexPolynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
    } else {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          final int nDegree = EndianTransformer.flip(is.readInt());
          final int dDegree = EndianTransformer.flip(is.readInt());
          final DoubleComplexNumber[] nCoef = new DoubleComplexNumber[nDegree + 1];
          for (int k = 0; k <= nDegree; k++) {
            final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            nCoef[k] = new DoubleComplexNumber(real, imag);
          }
          final DoubleComplexNumber[] dCoef = new DoubleComplexNumber[dDegree + 1];
          for (int k = 0; k <= dDegree; k++) {
            final double real = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            final double imag = Double.longBitsToDouble(EndianTransformer.flip(is.readLong()));
            dCoef[k] = new DoubleComplexNumber(real, imag);
          }
          ans[i][j] = new DoubleComplexRationalPolynomial(new DoubleComplexPolynomial(nCoef, "s"), new DoubleComplexPolynomial(dCoef, "s")); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
  
    }
    return ans;
  }

  /**
   * 行列をMMフォーマットの文字列に変換します。
   * 
   * @param matrix 対象となる行列
   * @param elementFormat 成分の出力フォーマット
   * @return MMフォーマットの文字列
   */
  public static String toMmString(final DoubleComplexRationalPolynomial[][] matrix, final String elementFormat) {
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
          final DoubleComplexRationalPolynomial element = matrix[i][j];
          final DoubleComplexPolynomial numerator = element.getNumerator();
          final DoubleComplexPolynomial denominator = element.getDenominator();
          final String[] nm = new String[]{numerator.toMmString(elementFormat), denominator.toMmString(elementFormat)};
          sb.append("("); //$NON-NLS-1$
          sb.append(nm[0]);
          sb.append(")/"); //$NON-NLS-1$
          sb.append(newLine);
          sb.append("  ("); //$NON-NLS-1$
          sb.append(nm[1]);
          sb.append(")"); //$NON-NLS-1$
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
   * 行列をMXフォーマットで出力ストリームに出力します。
   * 
   * @param matrix 対象となる行列
   * @param output 出力ストリーム
   * @param name 行列の名前
   * @throws IOException ストリームに出力できない場合
   */
  public static void writeMxFormat(final DoubleComplexRationalPolynomial[][] matrix, final DataOutputStream output, final String name) throws IOException {
    final MxDataHead head = new MxDataHead(matrix, name);
  
    head.write(output);
  
    final int rowSize = matrix.length;
    final int columnSize = matrix.length == 0 ? 0 : matrix[0].length;
  
    if (head.getMatrixType() == MxDataHead.REAL_RATIONAL_POLYNOMIAL_MATRIX) {
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt(matrix[i][j].getNumeratorDegree());
          output.writeInt(matrix[i][j].getDenominatorDegree());
          matrix[i][j].getNumerator().writeMxFormatWithoutHeader(output, false);
          matrix[i][j].getDenominator().writeMxFormatWithoutHeader(output, false);
        }
      }
    } else {
      // assert head.getMatrixType() == 3;
      for (int i = 0; i < rowSize; i++) {
        for (int j = 0; j < columnSize; j++) {
          output.writeInt(matrix[i][j].getNumeratorDegree());
          output.writeInt(matrix[i][j].getDenominatorDegree());
          matrix[i][j].getNumerator().writeMxFormatWithoutHeader(output, true);
          matrix[i][j].getDenominator().writeMxFormatWithoutHeader(output, true);
        }
      }
    }
  
    output.flush();
  }

  /**
   * Returns result of multiplication.
   * 
   * @param matrix 対象となる行列
   * @param polynomial polynomial
   * @return result of multiplication. 
   */
  public static DoubleComplexRationalPolynomial[][] multiply(final DoubleComplexRationalPolynomial[][] matrix, DoubleComplexPolynomial polynomial) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoubleComplexRationalPolynomial[][] ans = new DoubleComplexRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].multiply(polynomial);
      }
    }
    return ans;
  }

  /**
   * Returns result of multiplication.
   * 
   * @param matrix 対象となる行列
   * @param polynomial polynomial
   * @return result of multiplication.
   */
  public static DoubleComplexRationalPolynomial[][] divide(final DoubleComplexRationalPolynomial[][] matrix, DoubleComplexPolynomial polynomial) {
    final int rowSize = matrix.length;
    final int columnSize = rowSize == 0 ? 0 : matrix[0].length;
    final DoubleComplexRationalPolynomial[][] ans = new DoubleComplexRationalPolynomial[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = matrix[i][j].divide(polynomial);
      }
    }
    return ans;
  }

}
