package org.mklab.nfc.dae.doublePrecision;

import org.mklab.nfc.matrix.DoubleComplexMatrix;
import org.mklab.nfc.matrix.DoubleMatrix;
import org.mklab.nfc.scalar.DoubleComplexNumber;
import org.mklab.nfc.scalar.DoubleNumber;


/**
 * 微分代数方程式の解を求めるソルバーを表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2011/09/06
 */
public interface DoubleDifferentialAlgebraicEquationSolver {

  /**
   * t0秒からtf秒までの微分代数方程式の解を求めます。
   * 
   * @param equation 微分代数方程式
   * @param t0 初期時刻
   * @param tf 最終時刻
   * @param x0 状態xの初期値
   * @param dx0 状態xの導関数の初期値
   */
  void solve(DoubleDifferentialAlgebraicEquation equation, double t0, double tf, DoubleMatrix x0, DoubleMatrix dx0);

  /**
   * t0秒からtf秒までの微分代数方程式の解を求めます。
   * 
   * @param equation 微分代数方程式
   * @param matrix ヤコビ行列
   * @param t0 初期時刻
   * @param tf 最終時刻
   * @param x0 状態xの初期値
   * @param dx0 状態xの導関数の初期値
   */
  void solve(DoubleDifferentialAlgebraicEquation equation, DoubleJacobianMatrix matrix, double t0, double tf, DoubleMatrix x0, DoubleMatrix dx0);

  /**
   * t0秒からtf秒までの微分代数システムの解を求めます。
   * 
   * @param system 微分代数システム
   * @param t0 初期時刻
   * @param tf 最終時刻
   */
  void solve(DoubleDifferentialAlgebraicSystem<DoubleNumber,DoubleMatrix,DoubleComplexNumber,DoubleComplexMatrix> system, double t0, double tf);

  /**
   * シミュレーションの刻み幅を設定するメソッドです。
   * 
   * @param stepSize 刻み幅
   */
  void setStepSize(double stepSize);

  /**
   * 絶対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 絶対許容誤差
   */
  void setAbsoluteTolerance(double tolerance);

  /**
   * 絶対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 絶対許容誤差
   */
  void setAbsoluteTolerance(DoubleMatrix tolerance);

  /**
   * 相対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 相対許容誤差
   */
  void setRelativeTolerance(double tolerance);

  /**
   * 相対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 相対許容誤差
   */
  void setRelativeTolerance(DoubleMatrix tolerance);

  /**
   * 初期刻み幅を設定するメソッドです。
   * 
   * @param initialStepSize 初期刻み幅
   */
  void setInitialStepSize(double initialStepSize);

  /**
   * 微分代数方程式の解を返すメソッドです。
   * 
   * @return 微分代数方程式の解
   */
  DoubleMatrix getSolution();

}
