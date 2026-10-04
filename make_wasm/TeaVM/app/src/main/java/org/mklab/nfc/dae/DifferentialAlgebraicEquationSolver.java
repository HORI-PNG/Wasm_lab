package org.mklab.nfc.dae;

import org.mklab.nfc.matrix.ComplexNumericalMatrix;
import org.mklab.nfc.matrix.RealNumericalMatrix;
import org.mklab.nfc.scalar.ComplexNumericalScalar;
import org.mklab.nfc.scalar.RealNumericalScalar;


/**
 * 微分代数方程式の解を求めるソルバーを表すインターフェースです。
 * 
 * @author kageyama
 * @version $Revision$, 2011/09/06
 * 
 * @param <RS> type of real scalar
 * @param <RM> type of real matrix
 * @param <CS> type of complex scalar
 * @param <CM> type of complex matrix
 */
public interface DifferentialAlgebraicEquationSolver<RS extends RealNumericalScalar<RS,RM,CS,CM>, RM extends RealNumericalMatrix<RS,RM,CS,CM>, CS extends ComplexNumericalScalar<RS,RM,CS,CM>, CM extends ComplexNumericalMatrix<RS,RM,CS,CM>> {

  /**
   * t0秒からtf秒までの微分代数方程式の解を求めます。
   * 
   * @param equation 微分代数方程式
   * @param t0 初期時刻
   * @param tf 最終時刻
   * @param x0 状態xの初期値
   * @param dx0 状態xの導関数の初期値
   */
  void solve(TimeVaryingImplicitDifferentialAlgebraicEquation<RS,RM,CS,CM> equation, RS t0, RS tf, RM x0, RM dx0);

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
  void solve(TimeVaryingImplicitDifferentialAlgebraicEquation<RS,RM,CS,CM> equation, JacobianMatrix<RS,RM,CS,CM> matrix, RS t0, RS tf, RM x0, RM dx0);

  /**
   * t0秒からtf秒までの微分代数システムの解を求めます。
   * 
   * @param system 微分代数システム
   * @param t0 初期時刻
   * @param tf 最終時刻
   */
  void solve(DifferentialAlgebraicSystem<RS,RM,CS,CM> system, RS t0, RS tf);

  /**
   * シミュレーションの刻み幅を設定するメソッドです。
   * 
   * @param stepSize 刻み幅
   */
  void setStepSize(RS stepSize);

  /**
   * 絶対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 絶対許容誤差
   */
  void setAbsoluteTolerance(RS tolerance);

  /**
   * 絶対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 絶対許容誤差
   */
  void setAbsoluteTolerance(RM tolerance);

  /**
   * 相対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 相対許容誤差
   */
  void setRelativeTolerance(RS tolerance);

  /**
   * 相対許容誤差を設定するメソッドです。
   * 
   * @param tolerance 相対許容誤差
   */
  void setRelativeTolerance(RM tolerance);

  /**
   * 初期刻み幅を設定するメソッドです。
   * 
   * @param initialStepSize 初期刻み幅
   */
  void setInitialStepSize(RS initialStepSize);

  /**
   * 微分代数方程式の解を返すメソッドです。
   * 
   * @return 微分代数方程式の解
   */
  RM getSolution();
}
