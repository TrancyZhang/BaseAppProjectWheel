//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.project.base.app.tz.customview.tabview;

class MathUtils {
    MathUtils() {
    }

    static int constrain(int amount, int low, int high) {
        return amount < low?low:(amount > high?high:amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low?low:(amount > high?high:amount);
    }
}
