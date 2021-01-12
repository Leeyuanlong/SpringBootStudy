package com.feibai.leetcode.study.solution;

import java.util.*;

/**
 * 重点：滑动窗口法，查找最长不重复字符串
 * <p>
 * 使用空间换时间
 * <p>
 * 辅助数据结构：HashSet
 */
public class Lc_03_LongestSubstring {

  /**
   * 3. 无重复字符的最长子串
   * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
   * <p>
   * 示例 1:
   * <p>
   * 输入: "abcabcbb"
   * 输出: 3
   * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
   * 示例 2:
   * <p>
   * 输入: "bbbbb"
   * 输出: 1
   * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
   * 示例 3:
   * <p>
   * 输入: "pwwkew"
   * 输出: 3
   * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
   * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
   */

  public static void main(String[] args) {
    Lc_03_LongestSubstring instance = new Lc_03_LongestSubstring();
//    System.out.println(instance.lengthOfLongestSubstringByVolience(" "));
    System.out.println(instance.lengthOfLongestSubstringKMP_while("abcbbcbbefgbgfyue"));
  }

  // 滑动窗口:O(n)
  public int lengthOfLongestSubstringKMP_while(String s) {
    int length = s.length();
    Set<Character> set = new HashSet<>();
    int max_len = 0, i = 0, j = 0;
    while (i < length && j < length) {
      //调整[i,j]的范围
      if (!set.contains(s.charAt(j))) {
        set.add(s.charAt(j++));
        max_len = Math.max(max_len, j - i);
      } else {
        set.remove(s.charAt(i++));
      }
    }
    return max_len;
  }

  // 暴力解法: O(n^3)
  public int lengthOfLongestSubstringByVolience(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }

    int max = 0;
    for (int i = 0; i < s.length(); i++) {
      StringBuilder sb = new StringBuilder();
      int cnt = 0;
      for (int j = i; j < s.length(); j++) {
        if (sb.toString().contains(Character.toString(s.charAt(j)))) {//String.valueOf(s.charAt(j));
          break;
        }
        sb.append(s.charAt(j));
        cnt++;
      }
      if (cnt > max) {
        max = cnt;
      }
    }
    return max;
  }

}