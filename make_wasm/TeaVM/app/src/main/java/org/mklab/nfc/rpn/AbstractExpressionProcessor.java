/*
 * Created on 2008/09/09
 * Copyright (C) 2008 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.nfc.rpn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mklab.nfc.matrix.IntMatrix;
import org.mklab.nfc.matrix.Matrix;
import org.mklab.nfc.matrix.NumericalMatrix;
import org.mklab.nfc.matx.MatxObject;
import org.mklab.nfc.scalar.NumericalScalar;
import org.mklab.nfc.scalar.Scalar;


/**
 * 逆ポーランド記法を数式に関して評価する抽象クラスです。
 * 
 * @author koga
 * @version $Revision$, 2008/09/09
 * @param <S> スカラーの型
 * @param <M> 行列の型
 */
public abstract class AbstractExpressionProcessor<S extends Scalar<S,M>, M extends Matrix<S,M>> extends AbstractProcessor<S,M> implements ReversePolishNotationExpressionProcessor {

  /** trueならば、数値がゼロの数式を削除します。 */
  private boolean hasCancellation = true;

  /**
   * 数値がゼロの数式を削除するか判定します。
   * 
   * @return trueならば、数値がゼロの数式を削除します。
   */
  public boolean hasCancellation() {
    return this.hasCancellation;
  }

  /**
   * 数値がゼロの数式を削除するか設定します。
   * 
   * @param hasCancellation trueならば、数値がゼロの数式を削除します。
   */
  public void setHasCancellation(final boolean hasCancellation) {
    this.hasCancellation = hasCancellation;
  }

  /**
   * {@inheritDoc}
   */
  public String getResult(final ReversePolishNotationOperand<S,M> operand) {
    return getSignedExpression(evaluate(operand));
  }

  /**
   * 定数行列の数式を返します。
   * 
   * @param value 定数行列
   * @return 定数行列の数式
   */
  private String getExpression(final M value) {
    if (isScalar(value)) {
      if (value instanceof IntMatrix) {
        final int element = ((IntMatrix)value).getIntElement(1, 1);
        if (element < 0) {
          return getLeftParenthesis() + element + getRightParenthesis();
        }
        return "" + element; //$NON-NLS-1$
      }

      if (value instanceof NumericalMatrix<?,?>) {

        final NumericalScalar<?,?> element = ((NumericalMatrix<?,?>)value).getElement(1, 1);
        if (element.isZero()) {
          return "0"; //$NON-NLS-1$
        }
        if (element.isLessThan(0)) {
          return getLeftParenthesis() + element.toString(getFormat()) + getRightParenthesis();
        }

        return element.toString(getFormat());
      }
    }

    return ((MatxObject)value).toMmString(getFormat()).replaceAll("\\s*", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * 符号を反転したシンボルスタックを返します。
   * 
   * @param symbolStack シンボルのスタック
   * @return 符号を反転したシンボルスタック
   * 
   */
  private List<ReversePolishNotationSymbol<S,M>> invertSign(final List<ReversePolishNotationSymbol<S,M>> symbolStack) {
    final List<ReversePolishNotationSymbol<S,M>> signInvertedStack = new ArrayList<>();

    for (final ReversePolishNotationSymbol<S,M> symbol : symbolStack) {
      signInvertedStack.add(((ReversePolishNotationOperand<S,M>)symbol).invertSign());
    }

    return signInvertedStack;
  }

  /**
   * 符号を反転した値を返します。
   * 
   * @param operand オペランド
   * @return 符号を反転した値
   */
  private ReversePolishNotationOperand<S,M>  createSignInvertedOperand(final ReversePolishNotationOperand<S,M>  operand) {
    final M value = operand.getOperandValue().createClone();
    final ReversePolishNotationOperand<S,M>  ans = operand.createOperand(value);
    ans.setSymbolStack(Collections.unmodifiableList(operand.getSymbolStack()));
    ans.setVariable(operand.isVariable());
    ans.setNegative(!operand.isNegative());

    if (operand.isSingleTerm()) {
      ans.setExpression(operand.getExpression());
      ans.setSingleTerm(true);
      return ans;
    }

    if (operand.isNegative()) {
      ans.setExpression(operand.getExpression());
      ans.setSingleTerm(false);
      return ans;
    }

    ans.setExpression(getLeftParenthesis() + operand.getParsedOperand().getExpression() + getRightParenthesis());
    ans.setSingleTerm(false);
    return ans;
  }

  /**
   * オペランドの数式を返します。
   * 
   * @param operand 対象となるオペランド
   * @return オペランドの数式
   */
  private String getExpression(final ReversePolishNotationOperand<S,M>  operand) {
    if (operand.isVariable() == false) {
      return getExpression(operand.getOperandValue());
    }

    if (operand.isSingleTerm()) {
      return operand.getExpression();
    }

    return getLeftParenthesis() + operand.getParsedOperand().getExpression() + getRightParenthesis();
  }

  /**
   * オペランドの符号付数式を返します。
   * 
   * @param operand 対象となるオペランド
   * @return オペランドの符号付数式
   */
  private String getSignedExpression(final ReversePolishNotationOperand<S,M>  operand) {
    if (hasCancellation() && operand.isZeroOperand()) {
      return "0"; //$NON-NLS-1$
    }

    if (operand.isSingleTerm()) {
      return operand.getSignedExpression();
    }

    final String expression = operand.getParsedOperand().getExpression();

    if (operand.isNegative()) {
      return "-" + getLeftParenthesis() + expression + getRightParenthesis(); //$NON-NLS-1$
    }
    return expression;
  }

  /**
   * {@inheritDoc}
   */
  public ReversePolishNotationOperand<S,M>  inverseOperation(final ReversePolishNotationOperand<S,M>  operand) {
    final List<ReversePolishNotationSymbol<S,M>> operandStack = operand.getSymbolStack();
    if (operandStack.size() == 2 && operandStack.get(operandStack.size()-1) instanceof InverseOperator) {
      operandStack.remove(operandStack.size()-1);
      
      final ReversePolishNotationOperand<S,M>  inner = evaluate((ReversePolishNotationOperand<S,M>)operandStack.get(0));
      return inner;
    }
    
    final ReversePolishNotationOperand<S,M>  inner = evaluate(operand);

    if (inner.isUnitOperand() || inner.isNegativeUnitOperand()) {
      return inner;
    }

    final M value = inner.getOperandValue();
    final ReversePolishNotationOperand<S,M>  ans = operand.createOperand(value);
    ans.setNegative(inner.isNegative());

    if (inner.isSingleTerm() == false) {
      ans.setExpression(getLeftParenthesis() + inner.getParsedOperand().getExpression() + getRightParenthesis() + getInverseString());
    } else {
      if (inner.getSymbolStack().size() == 1 && ((ReversePolishNotationOperand<S,M>)inner.getSymbolStack().get(0)).getSymbolStack().size() == 1) {
        ans.setExpression(inner.getExpression() + getInverseString());
      } else {
        ans.setExpression(getLeftParenthesis() + inner.getExpression() + getRightParenthesis() + getInverseString());
      }
    }

    final List<ReversePolishNotationSymbol<S,M>> stack = new ArrayList<>();
    stack.addAll(operand.getSymbolStack());

    ans.setSymbolStack(stack);
    ans.setVariable(operand.isVariable());

    return ans;
  }
  

  /**
   * {@inheritDoc}
   */
  public ReversePolishNotationOperand<S,M> addOperation(final ReversePolishNotationOperand<S,M> left, final ReversePolishNotationOperand<S,M> right) {
    if (right.isZeroOperand()) {
      return left;
    }

    if (left.isZeroOperand()) {
      return right;
    }

    M leftValue = left.getOperandValue();
    if (left.isNegative()) {
      leftValue = leftValue.unaryMinus();
    }
    M rightValue = right.getOperandValue();
    if (right.isNegative()) {
      rightValue = rightValue.unaryMinus();
    }

    final M value = leftValue.add(rightValue);
    final ReversePolishNotationOperand<S,M> ans = left.createOperand(value);
    ans.getSymbolStack().remove(0);
    ans.setVariable(left.isVariable() || right.isVariable());

    if (hasCancellation() && value.isZero()) {
      ans.setExpression("0"); //$NON-NLS-1$
      return ans;
    }

    if (left.isVariable() == false && right.isVariable() == false) {
      if (value.isUnit()) {
        return left.createUnitOperand(value.getRowSize());
      }

      if (value.unaryMinus().isUnit()) {
        return left.createNegativeUnitOperand(value.getRowSize());
      }

      ans.setExpression(getExpression(value));
      return ans;
    }

    ReversePolishNotationOperand<S,M> signedLeft = left;
    ReversePolishNotationOperand<S,M> signedRight = right;

    if (left.isNegative() && right.isNegative()) {
      ans.setNegative(true);
      signedLeft = left.invertSign();
      signedRight = right.invertSign();
    }

    if (signedLeft.isSingleTerm()) {
      ans.addSymbol(signedLeft);
    } else {
      final List<ReversePolishNotationSymbol<S,M>> leftStack = signedLeft.getSymbolStack();
      if (signedLeft.isNegative()) {
        ans.addSymbols(invertSign(leftStack));
      } else {
        ans.addSymbols(leftStack);
      }
    }

    if (signedRight.isSingleTerm()) {
      ans.addSymbol(signedRight);
    } else {
      final List<ReversePolishNotationSymbol<S,M>> rightStack = signedRight.getSymbolStack();
      if (signedRight.isNegative()) {
        ans.addSymbols(invertSign(rightStack));
      } else {
        ans.addSymbols(rightStack);
      }
    }
    
    ans.setSingleTerm(false);
    ans.setExpression(null);

    return ans;
  }

  /**
   * {@inheritDoc}
   */
  public ReversePolishNotationOperand<S,M> multiplyOperation(final ReversePolishNotationOperand<S,M> left, final ReversePolishNotationOperand<S,M> right) {
    final M leftValue = left.getOperandValue();
    final boolean isLeftConstantUnit = left.isVariable() == false && left.isNegative() == false && leftValue.isUnit();
    if (left.isUnitOperand() || isLeftConstantUnit) {
      return right;
    }

    final M rightValue = right.getOperandValue();
    final boolean isRightConstantUnit = right.isVariable() == false && right.isNegative() == false && rightValue.isUnit();
    if (right.isUnitOperand() || isRightConstantUnit) {
      return left;
    }

    if (left.isNegativeUnitOperand() && right.isNegativeUnitOperand()) {
      return left.createUnitOperand(leftValue.getRowSize());
    }

    final boolean isLeftConstantNegativeUnit = left.isVariable() == false && left.isNegative() == false && leftValue.unaryMinus().isUnit();
    if (left.isNegativeUnitOperand() || isLeftConstantNegativeUnit) {
      return createSignInvertedOperand(right);
    }

    final boolean isRightConstantNegativeUnit = right.isVariable() == false && right.isNegative() == false && rightValue.unaryMinus().isUnit();
    if (right.isNegativeUnitOperand() || isRightConstantNegativeUnit) {
      return createSignInvertedOperand(left);
    }

    final M value = multiplyMatrixAndScalar(leftValue, rightValue);

    final ReversePolishNotationOperand<S,M> ans = left.createOperand(value);
    ans.setVariable(left.isVariable() || right.isVariable());

    final boolean hasSameSign = (left.isNegative() == right.isNegative());
    if (hasSameSign) {
      ans.setNegative(false);
    } else {
      ans.setNegative(true);
    }

    if (hasCancellation() && value.isZero()) {
      ans.setExpression("0"); //$NON-NLS-1$
      return ans;
    }

    if (left.isVariable() == false && right.isVariable() == false) {
      final boolean isUnit = (value.isUnit() && hasSameSign) || (value.unaryMinus().isUnit() && hasSameSign == false);
      if (isUnit) {
        return left.createUnitOperand(value.getRowSize());
      }

      final boolean isNegativeUnit = (value.isUnit() && hasSameSign == false) || (value.unaryMinus().isUnit() && hasSameSign);
      if (isNegativeUnit) {
        return left.createNegativeUnitOperand(value.getRowSize());
      }

      ans.setExpression(getExpression(value));
      return ans;
    }

    final List<ReversePolishNotationSymbol<S,M>> leftStack = left.getSymbolStack();
    final List<ReversePolishNotationSymbol<S,M>> rightStack = right.getSymbolStack();

    if (isSingleMultiplication(left) && right.isVariable() == false) {
      final ReversePolishNotationOperand<S,M> leftLeft = (ReversePolishNotationOperand<S,M>)leftStack.get(0);
      final ReversePolishNotationOperand<S,M> leftRight = (ReversePolishNotationOperand<S,M>)leftStack.get(1);

      if (leftRight.isVariable() == false) {
        final M newRightValue = multiplyMatrixAndScalar(leftRight.getOperandValue(), rightValue);
        final String rightExpression = getExpression(newRightValue);
        final ReversePolishNotationOperand<S,M>  newRight = right.createOperand(newRightValue);
        newRight.setExpression(rightExpression);
        newRight.setVariable(false);
        newRight.setNegative(false);

        createMultipliedOperand(ans, leftLeft, newRight);
        return ans;
      }

      if (leftLeft.isVariable() == false) {
        final M leftLeftValue = leftLeft.getOperandValue();
        if (isScalar(leftLeftValue)) {
          final M newRightValue = multiplyMatrixAndScalar(leftLeftValue, rightValue);
          final String rightExpression = getExpression(newRightValue);
          final ReversePolishNotationOperand<S,M>  newRight = right.createOperand(newRightValue);
          newRight.setExpression(rightExpression);
          newRight.setVariable(false);
          newRight.setNegative(false);

          createMultipliedOperand(ans, leftRight, newRight);
          return ans;
        }

        if (isScalar(rightValue)) {
          final M newLeftValue = multiplyMatrixAndScalar(leftLeftValue, rightValue);
          final String leftExpression = getExpression(newLeftValue);
          final ReversePolishNotationOperand<S,M>  newLeft = left.createOperand(newLeftValue);
          newLeft.setExpression(leftExpression);
          newLeft.setVariable(false);
          newLeft.setNegative(false);

          createMultipliedOperand(ans, newLeft, leftRight);
          return ans;
        }
      }
    }

    if (isSingleMultiplication(left) && rightStack.size() == 1) {
      final ReversePolishNotationOperand<S,M> leftLeft = (ReversePolishNotationOperand<S,M>)leftStack.get(0);
      final ReversePolishNotationOperand<S,M>  leftRight = (ReversePolishNotationOperand<S,M>)leftStack.get(1);

      createMultipliedOperand(ans, leftLeft, leftRight, right);
      return ans;
    }

    if (isSingleMultiplication(right) && left.isVariable() == false) {
      final ReversePolishNotationOperand<S,M>  rightLeft = (ReversePolishNotationOperand<S,M>)rightStack.get(0);
      final ReversePolishNotationOperand<S,M>  rightRight = (ReversePolishNotationOperand<S,M>)rightStack.get(1);

      if (rightLeft.isVariable() == false) {
        final M newLeftValue = multiplyMatrixAndScalar(leftValue, rightLeft.getOperandValue());
        final String leftExpression = getExpression(newLeftValue);
        final ReversePolishNotationOperand<S,M>  newLeft = left.createOperand(newLeftValue);
        newLeft.setExpression(leftExpression);
        newLeft.setVariable(false);
        newLeft.setNegative(false);

        createMultipliedOperand(ans, newLeft, rightRight);
        return ans;
      }
      
      if (rightRight.isVariable() == false) {
        final M rightRightValue = rightRight.getOperandValue();

        if (isScalar(leftValue)) {
          final M newRightValue = multiplyMatrixAndScalar(leftValue, rightRightValue);
          final String rightExpression = getExpression(newRightValue);
          final ReversePolishNotationOperand<S,M>  newRight = left.createOperand(newRightValue);
          newRight.setExpression(rightExpression);
          newRight.setVariable(false);
          newRight.setNegative(false);

          createMultipliedOperand(ans, rightLeft, newRight);
          return ans;
        }
      }
    }

    if (isSingleMultiplication(right) && leftStack.size() == 1) {
      final ReversePolishNotationOperand<S,M>  rightLeft = (ReversePolishNotationOperand<S,M>)rightStack.get(0);
      final ReversePolishNotationOperand<S,M>  rightRight = (ReversePolishNotationOperand<S,M>)rightStack.get(1);

      createMultipliedOperand(ans, left, rightLeft, rightRight);
      return ans;
    }

    if (isSingleMultiplication(left) && isSingleMultiplication(right)) {
      final ReversePolishNotationOperand<S,M>  leftLeft = (ReversePolishNotationOperand<S,M>)leftStack.get(0);
      final ReversePolishNotationOperand<S,M>  leftRight = (ReversePolishNotationOperand<S,M>)leftStack.get(1);
      final ReversePolishNotationOperand<S,M>  rightLeft = (ReversePolishNotationOperand<S,M>)rightStack.get(0);
      final ReversePolishNotationOperand<S,M>  rightRight = (ReversePolishNotationOperand<S,M>)rightStack.get(1);

      final M leftLeftValue = leftLeft.getOperandValue();
      final M leftRightValue = leftRight.getOperandValue();
      final M rightLeftValue = rightLeft.getOperandValue();
      final M rightRightValue = rightRight.getOperandValue();

      if (leftRight.isVariable() == false && rightLeft.isVariable() == false) {
        final M centerValue = multiplyMatrixAndScalar(leftRightValue, rightLeftValue);
        final String centerExpression = getExpression(centerValue);
        final ReversePolishNotationOperand<S,M>  center = left.createOperand(centerValue);
        center.setExpression(centerExpression);
        center.setVariable(false);
        center.setNegative(false);

        createMultipliedOperand(ans, leftLeft, center, rightRight);
        return ans;
      }

      if (leftLeft.isVariable() == false && isScalar(leftLeftValue) && rightLeft.isVariable() == false) {
        final M centerValue = multiplyMatrixAndScalar(leftLeftValue, rightLeftValue);
        final String centerExpression = getExpression(centerValue);
        final ReversePolishNotationOperand<S,M>  center = left.createOperand(centerValue);
        center.setExpression(centerExpression);
        center.setVariable(false);
        center.setNegative(false);

        createMultipliedOperand(ans, leftRight, center, rightRight);
        return ans;
      }

      if (leftLeft.isVariable() == false && isScalar(leftLeftValue) && rightRight.isVariable() == false) {
        final M newRightValue = multiplyMatrixAndScalar(leftLeftValue, rightRightValue);
        final String newRightExpression = getExpression(newRightValue);
        final ReversePolishNotationOperand<S,M>  newRight = left.createOperand(newRightValue);
        newRight.setExpression(newRightExpression);
        newRight.setVariable(false);
        newRight.setNegative(false);

        createMultipliedOperand(ans, leftRight, rightLeft, newRight);
        return ans;
      }

      if (rightLeft.isVariable() == false && isScalar(rightLeftValue) && leftLeft.isVariable() == false) {
        final M newLeftValue = multiplyMatrixAndScalar(leftLeftValue, rightLeftValue);
        final String newLeftExpression = getExpression(newLeftValue);
        final ReversePolishNotationOperand<S,M>  newLeft = left.createOperand(newLeftValue);
        newLeft.setExpression(newLeftExpression);
        newLeft.setVariable(false);
        newLeft.setNegative(false);

        createMultipliedOperand(ans, newLeft, leftRight, rightRight);
        return ans;
      }
      
      if (rightLeft.isVariable() == false && isScalar(rightLeftValue)) {
        createMultipliedOperand(ans, leftLeft, leftRight, rightLeft, rightRight);
        return ans;
      }

    }

    createMultipliedOperand(ans, left, right);
    return ans;
  }

  /**
   * 行列とスカラーの乗算を行います。
   * @param leftValue 第一項
   * @param rightValue 第二項
   * @return 計算結果
   */
  private M multiplyMatrixAndScalar(final M leftValue, final M rightValue) {
    final M value;
    if (isScalar(leftValue) && isScalar(rightValue) == false) {
      value = leftValue.createOnes(rightValue.getRowSize(), 1).multiply(leftValue).vectorToDiagonal().multiply(rightValue);
    } else if (isScalar(leftValue) == false && isScalar(rightValue)) {
      value = leftValue.multiply(rightValue.createOnes(leftValue.getColumnSize(), 1).multiply(rightValue).vectorToDiagonal());
    } else {
      value = leftValue.multiply(rightValue);      
    }
    return value;
  }

  /**
   * 乗算結果のオペランドを生成します。
   * 
   * @param ans 結果を代入するオペランド
   * @param left 左オペランド
   * @param center 中央オペランド
   * @param right 右オペランド
   */
  private void createMultipliedOperand(final ReversePolishNotationOperand<S,M>  ans, final ReversePolishNotationOperand<S,M>  left, final ReversePolishNotationOperand<S,M>  center,
      final ReversePolishNotationOperand<S,M> right) {
    final List<ReversePolishNotationSymbol<S,M>> stack = new ArrayList<>();

    ReversePolishNotationOperand<S,M>  newLeft = left;
    ReversePolishNotationOperand<S,M>  newCenter = center;
    ReversePolishNotationOperand<S,M>  newRight = right;

    if (center.isVariable() == false && isScalar(center.getOperandValue())) {
      newLeft = center;
      newCenter = left;
      newRight = right;
    } else if (right.isVariable() == false && isScalar(right.getOperandValue())) {
      newLeft = right;
      newCenter = left;
      newRight = center;
    }

    stack.add(newLeft);
    stack.add(newCenter);
    stack.add(newRight);

    final MultiplyOperator<S,M> multiplyOperator1 = new MultiplyOperator<>();
    final MultiplyOperator<S,M> multiplyOperator2 = new MultiplyOperator<>();
    stack.add(multiplyOperator1);
    stack.add(multiplyOperator2);

    final String leftExpression = getExpression(newLeft);
    final String centerExpression = getExpression(newCenter);
    final String rightExpression = getExpression(newRight);
    ans.setExpression(leftExpression + getMultiplicationString() + centerExpression + getMultiplicationString() + rightExpression);
    ans.setSymbolStack(stack);
  }

  /**
   * 乗算結果のオペランドを生成します。
   * 
   * @param ans 結果を代入するオペランド
   * @param leftLeft 左左オペランド
   * @param leftRight 左右オペランド
   * @param rightLeft 右左オペランド
   * @param rightRight 右右オペランド
   */
  private void createMultipliedOperand(final ReversePolishNotationOperand<S,M>  ans, final ReversePolishNotationOperand<S,M>  leftLeft, final ReversePolishNotationOperand<S,M>  leftRight,
      final ReversePolishNotationOperand<S,M>  rightLeft, final ReversePolishNotationOperand<S,M>  rightRight) {
    final List<ReversePolishNotationSymbol<S,M>> stack = new ArrayList<>();

    ReversePolishNotationOperand<S,M>  newLeftLeft = leftLeft;
    ReversePolishNotationOperand<S,M>  newLeftRight = leftRight;
    ReversePolishNotationOperand<S,M>  newRightLeft = rightLeft;
    ReversePolishNotationOperand<S,M>  newRightRight = rightRight;

    if (rightLeft.isVariable() == false && isScalar(rightLeft.getOperandValue())) {
      newLeftLeft = rightLeft;
      newLeftRight = leftLeft;
      newRightLeft = leftRight;
      newRightRight = rightRight;
    }

    stack.add(newLeftLeft);
    stack.add(newLeftRight);
    stack.add(newRightLeft);
    stack.add(newRightRight);

    final MultiplyOperator<S,M> multiplyOperator1 = new MultiplyOperator<>();
    final MultiplyOperator<S,M> multiplyOperator2 = new MultiplyOperator<>();
    final MultiplyOperator<S,M> multiplyOperator3 = new MultiplyOperator<>();

    stack.add(multiplyOperator1);
    stack.add(multiplyOperator2);
    stack.add(multiplyOperator3);

    final String leftLeftExpression = getExpression(newLeftLeft);
    final String leftRightExpression = getExpression(newLeftRight);
    final String rightLeftExpression = getExpression(newRightLeft);
    final String rightRightExpression = getExpression(newRightRight);
    ans.setExpression(leftLeftExpression + getMultiplicationString() + leftRightExpression + getMultiplicationString() + rightLeftExpression + getMultiplicationString() + rightRightExpression);
    ans.setSymbolStack(stack);
  }
  
  /**
   * スカラーであるか判定します。
   * 
   * @param value 対象となる行列
   * @return スカラーならばtrue、そうでなければfalse
   */
  private boolean isScalar(final M value) {
    return value.getRowSize() == 1 && value.getColumnSize() == 1;
  }

  /**
   * 乗算結果のオペランドを生成します。
   * 
   * @param ans 結果を代入するオペランド
   * @param left 左オペランド
   * @param right 右オペランド
   */
  private void createMultipliedOperand(final ReversePolishNotationOperand<S,M>  ans, final ReversePolishNotationOperand<S,M>  left, final ReversePolishNotationOperand<S,M>  right) {
    final List<ReversePolishNotationSymbol<S,M>> stack = new ArrayList<>();

    if (right.isVariable() == false && isScalar(right.getOperandValue())) {
      stack.add(right);
      stack.add(left);
      ans.setExpression(getExpression(right) + getMultiplicationString() + getExpression(left));
    } else {
      stack.add(left);
      stack.add(right);
      ans.setExpression(getExpression(left) + getMultiplicationString() + getExpression(right));
    }

    final MultiplyOperator<S,M> multiplyOperator = new MultiplyOperator<>();
    stack.add(multiplyOperator);
    ans.setSymbolStack(stack);
  }

  /**
   * 単一の乗算であるか判定します。
   * 
   * @param operand 対象となるオペランド
   * @return 単一の乗算ならばtrue、そうでなければfalse
   */
  private boolean isSingleMultiplication(final ReversePolishNotationOperand<S,M>  operand) {
    final List<ReversePolishNotationSymbol<S,M>> symbolStack = operand.getSymbolStack();
    if (symbolStack.size() != 3) {
      return false;
    }

    if (symbolStack.get(0) instanceof ReversePolishNotationOperand == false) {
      return false;
    }
    if (symbolStack.get(1) instanceof ReversePolishNotationOperand == false) {
      return false;
    }
    if (symbolStack.get(2) instanceof MultiplyOperator == false) {
      return false;
    }

    return true;
  }
}
