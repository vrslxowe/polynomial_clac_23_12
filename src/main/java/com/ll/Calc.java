package com.ll;

public class Calc {
  public static int run(String exp) { // 10 + (10 + 5)
    exp = exp.trim();//문자 사이 공백을 없애는 기능
    exp = stripOuterBracket(exp);//괄호를 없애주는 기능

    // 연산기호가 없으면 바로 리턴
    if (!exp.contains(" ")) return Integer.parseInt(exp);

    boolean needToMultiply = exp.contains(" * ");//exp에 곱셈 기호가 있어서 트루일때
    boolean needToPlus = exp.contains(" + ") || exp.contains(" - ");//더하기나 빼기 기호가 하나라도 있어서 참일때

    boolean needToCompound = needToMultiply && needToPlus; //위의 변수들이 둘 다 참일때
    boolean needToSplit = exp.contains("(") || exp.contains(")");//괄호가 하나라도 있어서 참일 때

    if (needToSplit) {  // 800 + (10 + 5) 스플릿이 참일때 실행

      int splitPointIndex = findSplitPointIndex(exp); //exp의 인덱스를 찾아서

      String firstExp = exp.substring(0, splitPointIndex);
      String secondExp = exp.substring(splitPointIndex + 1);

      char operator = exp.charAt(splitPointIndex);

      exp = Calc.run(firstExp) + " " + operator + " " + Calc.run(secondExp);

      return Calc.run(exp); //exp 값을 돌려줄때

    } else if (needToCompound) {
      String[] bits = exp.split(" \\+ "); //더하기 기호를 자르고 넣기

      return Integer.parseInt(bits[0]) + Calc.run(bits[1]); // 정수로 변환한 값을 돌려줄때 // TODO
    }
    if (needToPlus) {
      exp = exp.replaceAll("\\- ", "\\+ \\-"); //빼기 기호를 더하기 기호로 바꿔주는 기능

      String[] bits = exp.split(" \\+ "); // 더하기 기호 기준으로 자르는 기능

      int sum = 0; //sum변수에 0 값 넣기

      for (int i = 0; i < bits.length; i++) { //0부터 문자열의 길이 미만까지 반복 후 i값을 sum에 더해준 후 1 증가
        sum += Integer.parseInt(bits[i]);
      }

      return sum;
    } else if (needToMultiply) { // 곱하기 기호를 기준으로 자르는 기능
      String[] bits = exp.split(" \\* ");

      int rs = 1;

      for (int i = 0; i < bits.length; i++) { //0부터 문자열의 길이 미만까지 반복 후 i값을 sum에 곱해준 후 1 증가
        rs *= Integer.parseInt(bits[i]);
      }
      return rs;
    }

    throw new RuntimeException("처리할 수 있는 계산식이 아닙니다"); //해당하는 경우가 없을 때 나오는 기능
  }

  private static int findSplitPointIndexBy(String exp, char findChar) { //괄호를 찾을 때
    int bracketCount = 0;

    for (int i = 0; i < exp.length(); i++) { //i가 문자열의 길이 미만일때까지 반복하며 1 증가
      char c = exp.charAt(i);

      if (c == '(') { // 괄호를 찾았을 때 카운트 1 증가하고 다시 반복문으로 돌아감
        bracketCount++;
      } else if (c == ')') { //괄호를 찾았을 때 카운트  1감소하고 다시 반복문으로 돌아감
        bracketCount--;
      } else if (c == findChar) { //더하기 곱하기 연산자를 찾았을 때 카운트가 0과 같다면 i값을 돌려줌
        if (bracketCount == 0) return i;
      }
    }
    return -1; //거짓일 때 -1 해서 돌려줌
  }

  private static int findSplitPointIndex(String exp) {
    int index = findSplitPointIndexBy(exp, '+'); // 더하기 기호 찾고 인덱스 변수에 넣음

    if (index >= 0) return index; //

    return findSplitPointIndexBy(exp, '*'); // 곱하기 기호 찾아서 돌려줌
  }

  private static String stripOuterBracket(String exp) { //괄호를 없앨 때  사용하는 클래스
    int outerBracketCount = 0; // 0이라는 값을 넣음

    while (exp.charAt(outerBracketCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketCount) == ')') {
      outerBracketCount++; //카운트가 0,
    }

    if (outerBracketCount == 0) return exp;


    return exp.substring(outerBracketCount, exp.length() - outerBracketCount);
  }
}