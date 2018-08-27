package com.bayview.exam.enums;

import java.util.Random;

/**
 * Created by romelquitasol on 22/08/2018.
 */
public enum TrafficCondition {

  HEAVY,
  LIGHT,
  MODERATE;

  private static final TrafficCondition[] VALUES = values();
  private static final int SIZE = VALUES.length;
  private static final Random RANDOM = new Random();

  public static TrafficCondition randomCondition()  {
    return VALUES[RANDOM.nextInt(SIZE)];
  }

}
