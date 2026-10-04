package org.mklab.nfc.svd;

/**
 * 複素数を表すクラスです。
 * 
 * @author koga
 */
class ComplexValue {

  /** 実部。 */
  double re;

  /** 虚部。 */
  double im;

  /**
   * 新しく生成された<code>ComplexValue</code>オブジェクトを初期化します。
   * @param re 実部
   * @param im 虚部
   */
  ComplexValue(final double re, final double im) {
    this.re = re;
    this.im = im;
  }

  /**
   * 絶対値を返します。
   * 
   * @param c 対象となる複素数
   * @return 絶対値
   */
  static double cdabs(final ComplexValue c) {
    return Math.sqrt(c.re * c.re + c.im * c.im);
  }

  /**
   * 絶対値を返します。
   * 
   * @param c 対象となる複素数
   * @return 絶対値
   */
  static double cabs1(final ComplexValue c) {
    return (Math.abs(c.re) + Math.abs(c.im));
  }

  /**
   * ベクトルの方向がbと同じ、大きさがaと同じ複素数を返します。
   * 
   * @param ans 答えを代入する変数
   * @param a 対象となる複素数
   * @param b 対象となる複素数
   * @return ベクトルの方向がbと同じ、大きさがaと同じ複素数
   */
  static ComplexValue csign(final ComplexValue ans, final ComplexValue a, final ComplexValue b) {
    return multiply(ans, b, cdabs(a) / cdabs(b));
  }

  /**
   * 値を代入します。
   * 
   * @param a 代入する複素数
   * @param re 実部
   * @param im 虚部
   */
  static void setValue(final ComplexValue a, final double re, final double im) {
    a.re = re;
    a.im = im;
  }

  /**
   * ゼロを代入します。
   * 
   * @param a 代入する複素数
   */
  static void setZero(final ComplexValue a) {
    setValue(a, 0, 0);
  }

  /**
   * １を代入します。
   * 
   * @param a 代入する複素数
   */
  static void setUnit(final ComplexValue a) {
    setValue(a, 1, 0);
  }

  /**
   * 共役複素数を返します。
   * 
   * @param ans 共役複素数を代入する値
   * @param b 共役複素数を求める対象
   * @return 共役複素数
   */
  static ComplexValue conjugate(final ComplexValue ans, final ComplexValue b) {
    ans.re = b.re;
    ans.im = -b.im;
    return ans;
  }

  /**
   * 共役複素数を返します。
   * 
   * @param a 共役複素数を求める対象
   * @return 共役複素数
   */
  static ComplexValue conjugateSelf(final ComplexValue a) {
    a.im = -a.im;
    return a;
  }

  /**
   * 共役複素転置行列を返します。
   * 
   * @param a 複素数
   * @return 共役複素転置行列
   */
  static ComplexValue[][] conjugateTranspose(final ComplexValue[][] a) {
    final int rowSize = a.length;
    final int columnSize = a[0].length;
    final ComplexValue[][] ct = new ComplexValue[columnSize][rowSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ct[j][i] = new ComplexValue(a[i][j].re, -a[i][j].im);
      }
    }
    return ct;
  }

  /**
   * コピーを生成します。
   * 
   * @param a 複素行列
   * @return 行列のコピー
   */
  static ComplexValue[][] duplicate(final ComplexValue[][] a) {
    final int rowSize = a.length;
    final int columnSize = a[0].length;
    final ComplexValue[][] ans = new ComplexValue[rowSize][columnSize];
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        ans[i][j] = new ComplexValue(a[i][j].re, a[i][j].im);
      }
    }
    return ans;
  }

  /**
   * 複素数をコピーします。
   * 
   * @param to コピー先
   * @param from コピー元
   */
  static void copy(final ComplexValue to, final ComplexValue from) {
    to.re = from.re;
    to.im = from.im;
  }

  /**
   * 複素行列をコピーします。
   * 
   * @param to コピー先
   * @param from コピー元
   */
  static void copy(final ComplexValue[][] to, final ComplexValue[][] from) {
    final int rowSize = to.length;
    final int columnSize = to[0].length;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < columnSize; j++) {
        to[i][j].re = from[i][j].re;
        to[i][j].im = from[i][j].im;
      }
    }
  }

  /**
   * 足し算をします。
   * 
   * @param ans 計算結果
   * @param b 足す数
   * @param c 足される数
   */
  static void add(final ComplexValue ans, final ComplexValue b, final ComplexValue c) {
    ans.re = b.re + c.re;
    ans.im = b.im + c.im;
  }

  /**
   * 自身に複素数を加えます。
   * 
   * @param ans 加えられる数
   * @param b 加える数
   */
  static void addSelf(final ComplexValue ans, final ComplexValue b) {
    ans.re += b.re;
    ans.im += b.im;
  }

  /**
   * 掛け算を行います。
   * 
   * @param ans 計算結果
   * @param b 掛けられる数
   * @param c 掛けるか数
   */
  static void multiply(final ComplexValue ans, final double b, final ComplexValue c) {
    ans.re = b * c.re;
    ans.im = b * c.im;
  }

  /**
   * 掛け算を行います。
   * 
   * @param ans 計算結果
   * @param a 掛けられる数
   * @param b 掛ける数
   * @return 計算結果
   */
  static ComplexValue multiply(final ComplexValue ans, final ComplexValue a, final double b) {
    ans.re = a.re * b;
    ans.im = a.im * b;
    return ans;
  }

  /**
   * 掛け算を行います。
   * 
   * @param ans 計算結果
   * @param b 掛けられる数
   * @param c 掛ける数
   * @return 計算結果
   */
  static ComplexValue multiply(final ComplexValue ans, final ComplexValue b, final ComplexValue c) {
    ans.re = b.re * c.re - b.im * c.im;
    ans.im = b.re * c.im + b.im * c.re;
    return ans;
  }

  /**
   * 掛け算を行い、結果をaに代入します。
   * 
   * @param a 掛けられる複素数
   * @param b 掛ける複素数
   * @return 計算結果
   */
  static ComplexValue multiplylSelf(final ComplexValue a, final ComplexValue b) {
    final double tmp = a.re * b.re - a.im * b.im;
    a.im = a.re * b.im + a.im * b.re;
    a.re = tmp;
    return a;
  }

  /**
   * 自身に掛けます。
   * 
   * @param ans 掛けられる数
   * @param b 掛ける数
   * @return 計算結果
   */
  static ComplexValue multiplySelf(final ComplexValue ans, final double b) {
    ans.re *= b;
    ans.im *= b;
    return ans;
  }

  /**
   * 符号を反転します。
   * 
   * @param a 複素数
   * @return 符号を反転した複素数
   */
  static ComplexValue negateSelf(final ComplexValue a) {
    a.re = -a.re;
    a.im = -a.im;
    return a;
  }

  /**
   * 符号を反転した複素数を代入します。
   * 
   * @param ans 計算結果
   * @param b 複素数
   * @return 計算結果
   */
  static ComplexValue negate(final ComplexValue ans, final ComplexValue b) {
    ans.re = -b.re;
    ans.im = -b.im;
    return ans;
  }

  /**
   * 割り算を行います。
   * 
   * @param ans 計算結果
   * @param a 割られる数
   * @param b 割る数
   * @return 計算結果
   */
  static ComplexValue divide(final ComplexValue ans, final ComplexValue a, final ComplexValue b) {
    if ((Math.abs(b.re) + Math.abs(b.im)) == 0.0) {
      throw new RuntimeException(Messages.getString("ComplexValue.1")); //$NON-NLS-1$
    }

    if (Math.abs(b.re) > Math.abs(b.im)) {
      final double d = b.im / b.re;
      final double s = b.re + b.im * d;
      ans.re = (a.re + a.im * d) / s;
      ans.im = (-a.re * d + a.im) / s;
    } else {
      final double d = b.re / b.im;
      final double s = b.re * d + b.im;
      ans.re = (a.re * d + a.im) / s;
      ans.im = (-a.re + a.im * d) / s;
    }
    return ans;
  }

  /**
   * 割り算を行います。
   * 
   * @param ans 計算結果
   * @param b 割る数
   * @return 計算結果
   */
  static ComplexValue divideSelf(final ComplexValue ans, final ComplexValue b) {
    if ((Math.abs(b.re) + Math.abs(b.im)) == 0.0) {
      throw new RuntimeException(Messages.getString("ComplexValue.1")); //$NON-NLS-1$
    }

    if (Math.abs(b.re) > Math.abs(b.im)) {
      final double d = b.im / b.re;
      final double s = b.re + b.im * d;
      final double dr = (ans.re + ans.im * d) / s;
      ans.im = (-ans.re * d + ans.im) / s;
      ans.re = dr;
    } else {
      final double d = b.re / b.im;
      final double s = b.re * d + b.im;
      final double dr = (ans.re * d + ans.im) / s;
      ans.im = (-ans.re + ans.im * d) / s;
      ans.re = dr;
    }
    return ans;
  }

  /**
   * 逆数の複素数を代入します。
   * 
   * @param ans 計算結果
   * @param b 複素数
   * @return 計算結果
   */
  static ComplexValue inverse(final ComplexValue ans, final ComplexValue b) {
    if (Math.abs(b.re) + Math.abs(b.im) == 0.0) {
      throw new RuntimeException(Messages.getString("ComplexValue.2")); //$NON-NLS-1$
    }

    final double denominator = b.re * b.re + b.im * b.im;
    ans.re = b.re / denominator;
    ans.im = -b.im / denominator;
    return ans;
  }

  /**
   * 値を入れ替えます。
   * 
   * @param a 複素数
   * @param b 複素数
   */
  static void swap(final ComplexValue a, final ComplexValue b) {
    final double tmp1 = a.re;
    a.re = b.re;
    b.re = tmp1;
    
    final double tmp2 = a.im;
    a.im = b.im;
    b.im = tmp2;
  }
}