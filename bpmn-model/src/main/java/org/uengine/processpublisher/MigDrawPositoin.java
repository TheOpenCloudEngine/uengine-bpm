package org.uengine.processpublisher;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;

import java.util.Vector;

public class MigDrawPositoin{

    //ROLE Group
    private static int roleXPosition = 100;
    private static int roleYPosition = 100;
    private static int childRoleXPosition = roleXPosition + 10;
    private static int childRoleYPosition = roleYPosition;
    private static int childRoleWidth =  150;
    private static int childRoleHeigth = 150;
    private static int parentRoleNameLabelWidth = 20;

    //ACTIVITY Group
    private static int activityIntervalX = 120;
    private static int activityIntervalY = 100;

    //startEvent
    private static int startEventXPosition = 263;
    private static int startEventYPosition = 187;
    private static int startEventWidth = 30;
    private static int startEventHeight = 30;

    //startEvent
    private static int endEventXPosition = 0;
    private static int endEventYPosition = 0;
    private static int endEventWidth = 30;
    private static int endEventHeight = 30;

    //humanActivity
    private static int humanActivityWidth = 100;
    private static int humanActivityHeight = 100;

    //switchActivity



    public static int getRoleXPosition() {
        return roleXPosition;
    }

    public static void setRoleXPosition(int roleXPosition) {
        MigDrawPositoin.roleXPosition = roleXPosition;
    }

    public static int getRoleYPosition() {
        return roleYPosition;
    }

    public static void setRoleYPosition(int roleYPosition) {
        MigDrawPositoin.roleYPosition = roleYPosition;
    }

    public static int getChildRoleXPosition() {
        return childRoleXPosition;
    }

    public static void setChildRoleXPosition(int childRoleXPosition) {
        MigDrawPositoin.childRoleXPosition = childRoleXPosition;
    }

    public static int getChildRoleYPosition() {
        return childRoleYPosition;
    }

    public static void setChildRoleYPosition(int childRoleYPosition) {
        MigDrawPositoin.childRoleYPosition = childRoleYPosition;
    }

    public static int getChildRoleWidth() {
        return childRoleWidth;
    }

    public static void setChildRoleWidth(int childRoleWidth) {
        MigDrawPositoin.childRoleWidth = childRoleWidth;
    }

    public static int getChildRoleHeigth() {
        return childRoleHeigth;
    }

    public static void setChildRoleHeigth(int childRoleHeigth) {
        MigDrawPositoin.childRoleHeigth = childRoleHeigth;
    }


    public static int getStartEventXPosition() {
        return startEventXPosition;
    }

    public static void setStartEventXPosition(int startEventXPosition) {
        MigDrawPositoin.startEventXPosition = startEventXPosition;
    }

    public static int getStartEventYPosition() {
        return startEventYPosition;
    }

    public static void setStartEventYPosition(int startEventYPosition) {
        MigDrawPositoin.startEventYPosition = startEventYPosition;
    }

    public static int getStartEventWidth() {
        return startEventWidth;
    }

    public static void setStartEventWidth(int startEventWidth) {
        MigDrawPositoin.startEventWidth = startEventWidth;
    }

    public static int getStartEventHeight() {
        return startEventHeight;
    }

    public static void setStartEventHeight(int startEventHeight) {
        MigDrawPositoin.startEventHeight = startEventHeight;
    }


    public static int getHumanActivityWidth() {
        return humanActivityWidth;
    }

    public static void setHumanActivityWidth(int humanActivityWidth) {
        MigDrawPositoin.humanActivityWidth = humanActivityWidth;
    }

    public static int getHumanActivityHeight() {
        return humanActivityHeight;
    }

    public static void setHumanActivityHeight(int humanActivityHeight) {
        MigDrawPositoin.humanActivityHeight = humanActivityHeight;
    }

    public static int getActivityIntervalX() {
        return activityIntervalX;
    }

    public static void setActivityIntervalX(int activityIntervalX) {
        MigDrawPositoin.activityIntervalX = activityIntervalX;
    }

    public static int getActivityIntervalY() {
        return activityIntervalY;
    }

    public static void setActivityIntervalY(int activityIntervalY) {
        MigDrawPositoin.activityIntervalY = activityIntervalY;
    }

    public static int getEndEventXPosition() {
        return endEventXPosition;
    }

    public static void setEndEventXPosition(int endEventXPosition) {
        MigDrawPositoin.endEventXPosition = endEventXPosition;
    }

    public static int getEndEventYPosition() {
        return endEventYPosition;
    }

    public static void setEndEventYPosition(int endEventYPosition) {
        MigDrawPositoin.endEventYPosition = endEventYPosition;
    }

    public static int getEndEventWidth() {
        return endEventWidth;
    }

    public static void setEndEventWidth(int endEventWidth) {
        MigDrawPositoin.endEventWidth = endEventWidth;
    }

    public static int getEndEventHeight() {
        return endEventHeight;
    }

    public static void setEndEventHeight(int endEventHeight) {
        MigDrawPositoin.endEventHeight = endEventHeight;
    }

    public static int getParentRoleNameLabelWidth() {
        return parentRoleNameLabelWidth;
    }

    public static void setParentRoleNameLabelWidth(int parentRoleNameLabelWidth) {
        MigDrawPositoin.parentRoleNameLabelWidth = parentRoleNameLabelWidth;
    }
}
