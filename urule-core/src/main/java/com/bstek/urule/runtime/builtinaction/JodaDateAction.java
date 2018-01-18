/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2015年11月27日
 */
@ActionBean(name = "Joda日期")
public class JodaDateAction {

    @ActionMethod(name = "解析字符串为日期")
    @ActionMethodParameter(names = {"日期字符串", "格式"})
    public LocalDate parseLocalDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        try {
            Date date = sd.parse(dateStr);
            return new LocalDate(date.getTime());
        } catch (ParseException e) {
            throw new RuleException(e);
        }
    }

    @ActionMethod(name = "解析字符串为日期时间")
    @ActionMethodParameter(names = {"日期字符串", "格式"})
    public LocalDateTime parseLocalDateTime(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        try {
            Date date = sd.parse(dateStr);
            return new LocalDateTime(date.getTime());
        } catch (ParseException e) {
            throw new RuleException(e);
        }
    }

    @ActionMethod(name = "当前日期")
    @ActionMethodParameter(names = {})
    public LocalDate getDate() {
        return new LocalDate();
    }

    @ActionMethod(name = "当前日期时间")
    @ActionMethodParameter(names = {})
    public LocalDateTime getDateTime() {
        return new LocalDateTime();
    }

    @ActionMethod(name = "格式化日期")
    @ActionMethodParameter(names = {"目标日期", "格式"})
    public String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.toString(pattern);
    }

    @ActionMethod(name = "格式化日期")
    @ActionMethodParameter(names = {"目标日期", "格式"})
    public String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toString(pattern);
    }

    @ActionMethod(name = "加日期时间")
    @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数", "小时", "分钟", "秒数"})
    public LocalDateTime addDate(LocalDateTime dateTime, int years, int months, int days, int hours, int minutes, int seconds) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusYears(years)
                .plusMonths(months)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);
    }

    @ActionMethod(name = "加日期")
    @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数"})
    public LocalDate addDate(LocalDate date, int years, int months, int days) {
        if (date == null) {
            return null;
        }

        return date.plusYears(years)
                .plusMonths(months)
                .plusDays(days);
    }

    @ActionMethod(name = "日期时间加年")
    @ActionMethodParameter(names = {"目标日期", "年数"})
    public LocalDateTime addDateForYear(LocalDateTime dateTime, int years) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusYears(years);
    }

    @ActionMethod(name = "日期加年")
    @ActionMethodParameter(names = {"目标日期", "年数"})
    public LocalDate addDateForYear(LocalDate date, int years) {
        if (date == null) {
            return null;
        }

        return date.plusYears(years);
    }

    @ActionMethod(name = "日期时间加月")
    @ActionMethodParameter(names = {"目标日期", "月数"})
    public LocalDateTime addDateForMonth(LocalDateTime dateTime, int months) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusMonths(months);
    }

    @ActionMethod(name = "日期加月")
    @ActionMethodParameter(names = {"目标日期", "月数"})
    public LocalDate addDateForMonth(LocalDate date, int months) {
        if (date == null) {
            return null;
        }

        return date.plusMonths(months);
    }

    @ActionMethod(name = "日期时间加天")
    @ActionMethodParameter(names = {"目标日期", "天数"})
    public LocalDateTime addDateForDay(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusDays(days);
    }

    @ActionMethod(name = "日期加天")
    @ActionMethodParameter(names = {"目标日期", "天数"})
    public LocalDate addDateForDay(LocalDate date, int days) {
        if (date == null) {
            return null;
        }

        return date.plusDays(days);
    }

    @ActionMethod(name = "日期时间加小时")
    @ActionMethodParameter(names = {"目标日期", "小时数"})
    public LocalDateTime addDateForHour(LocalDateTime dateTime, int hours) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusHours(hours);
    }

    @ActionMethod(name = "日期时间加分钟")
    @ActionMethodParameter(names = {"目标日期", "分钟数"})
    public LocalDateTime addDateForMinute(LocalDateTime dateTime, int minutes) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusMinutes(minutes);
    }

    @ActionMethod(name = "日期时间加秒")
    @ActionMethodParameter(names = {"目标日期", "秒数"})
    public LocalDateTime addDateForSecond(LocalDateTime dateTime, int seconds) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.plusHours(seconds);
    }

    @ActionMethod(name = "日期时间减日期")
    @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数", "小时", "分钟", "秒数"})
    public LocalDateTime subDate(LocalDateTime dateTime, int years, int months, int days, int hours, int minutes, int seconds) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusYears(years)
                .minusMonths(months)
                .minusDays(days)
                .minusHours(hours)
                .minusMinutes(minutes)
                .minusSeconds(seconds);
    }

    @ActionMethod(name = "日期时间减日期")
    @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数"})
    public LocalDate subDate(LocalDate date, int years, int months, int days) {
        if (date == null) {
            return null;
        }
        return date.minusYears(years)
                .minusMonths(months)
                .minusDays(days);
    }

    @ActionMethod(name = "日期时间减年")
    @ActionMethodParameter(names = {"目标日期", "年数"})
    public LocalDateTime subDateForYear(LocalDateTime dateTime, int years) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusYears(years);
    }

    @ActionMethod(name = "日期减年")
    @ActionMethodParameter(names = {"目标日期", "年数"})
    public LocalDate subDateForYear(LocalDate date, int years) {
        if (date == null) {
            return null;
        }

        return date.minusYears(years);
    }

    @ActionMethod(name = "日期时间减月")
    @ActionMethodParameter(names = {"目标日期", "月数"})
    public LocalDateTime subDateForMonth(LocalDateTime dateTime, int months) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusMonths(months);
    }

    @ActionMethod(name = "日期减月")
    @ActionMethodParameter(names = {"目标日期", "月数"})
    public LocalDate subDateForMonth(LocalDate date, int months) {
        if (date == null) {
            return null;
        }

        return date.minusMonths(months);
    }

    @ActionMethod(name = "日期时间减天")
    @ActionMethodParameter(names = {"目标日期", "天数"})
    public LocalDateTime subDateForDay(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusDays(days);
    }

    @ActionMethod(name = "日期减天")
    @ActionMethodParameter(names = {"目标日期", "天数"})
    public LocalDate subDateForDay(LocalDate date, int days) {
        if (date == null) {
            return null;
        }

        return date.minusDays(days);
    }

    @ActionMethod(name = "日期时间减小时")
    @ActionMethodParameter(names = {"目标日期", "小时"})
    public LocalDateTime subDateForHour(LocalDateTime dateTime, int hours) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusHours(hours);
    }

    @ActionMethod(name = "日期时间减分钟")
    @ActionMethodParameter(names = {"目标日期", "分钟"})
    public LocalDateTime subDateForMinute(LocalDateTime dateTime, int minutes) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusMinutes(minutes);
    }

    @ActionMethod(name = "减日期减秒")
    @ActionMethodParameter(names = {"目标日期", "秒数"})
    public LocalDateTime subDateForSecond(LocalDateTime dateTime, int seconds) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.minusSeconds(seconds);
    }

    @ActionMethod(name = "取日期时间的年份")
    @ActionMethodParameter(names = {"目标日期"})
    public int getYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }

        return dateTime.getYear();
    }

    @ActionMethod(name = "取日期的年份")
    @ActionMethodParameter(names = {"目标日期"})
    public int getYear(LocalDate date) {
        if (date == null) {
            return 0;
        }

        return date.getYear();
    }

    @ActionMethod(name = "取日期时间的月份")
    @ActionMethodParameter(names = {"目标日期"})
    public int getMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }

        return dateTime.getMonthOfYear();
    }

    @ActionMethod(name = "取日期的月份")
    @ActionMethodParameter(names = {"目标日期"})
    public int getMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }

        return date.getMonthOfYear();
    }

    @ActionMethod(name = "取日期时间的星期几")
    @ActionMethodParameter(names = {"目标日期"})
    public int getWeekDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return -1;
        }

        return dateTime.getDayOfWeek();
    }

    @ActionMethod(name = "取日期的星期几")
    @ActionMethodParameter(names = {"目标日期"})
    public int getWeek(LocalDate date) {
        if (date == null) {
            return -1;
        }

        return date.getDayOfWeek();
    }

    @ActionMethod(name = "取日期时间在月份中的第几天")
    @ActionMethodParameter(names = {"目标日期"})
    public int getDayOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }

        return dateTime.getDayOfMonth();
    }

    @ActionMethod(name = "取日期在月份中的第几天")
    @ActionMethodParameter(names = {"目标日期"})
    public int getDayOfMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }

        return date.getDayOfMonth();
    }


    @ActionMethod(name = "取日期时间在年度中的第几天")
    @ActionMethodParameter(names = {"目标日期"})
    public int getDayOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }

        return dateTime.getDayOfYear();
    }

    @ActionMethod(name = "取日期在年度中的第几天")
    @ActionMethodParameter(names = {"目标日期"})
    public int getDayOfYear(LocalDate date) {
        if (date == null) {
            return 0;
        }

        return date.getDayOfYear();
    }

    @ActionMethod(name = "取日期时间的小时")
    @ActionMethodParameter(names = {"目标日期"})
    public int getHour(LocalDateTime dateTime) {
        if (dateTime == null) {
            return -1;
        }

        return dateTime.getHourOfDay();
    }

    @ActionMethod(name = "取日期时间的分钟")
    @ActionMethodParameter(names = {"目标日期"})
    public int getMinute(LocalDateTime dateTime) {
        if (dateTime == null) {
            return -1;
        }

        return dateTime.getMinuteOfHour();
    }

    @ActionMethod(name = "取日期时间的秒")
    @ActionMethodParameter(names = {"目标日期"})
    public int getSecond(LocalDateTime dateTime) {
        if (dateTime == null) {
            return -1;
        }

        return dateTime.getSecondOfMinute();
    }

    @ActionMethod(name = "日期时间相减返回毫秒")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifMillSecond(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.millis());
        return period.getMillis();
    }

    @ActionMethod(name = "日期相减返回毫秒")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifMillSecond(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.millis());
        return period.getMillis();
    }

    @ActionMethod(name = "日期时间相减返回秒")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifSecond(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.seconds());
        return period.getSeconds();
    }

    @ActionMethod(name = "日期相减返回秒")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifSecond(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.seconds());
        return period.getSeconds();
    }

    @ActionMethod(name = "日期时间相减返回分钟")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifMinute(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.minutes());
        return period.getMinutes();
    }

    @ActionMethod(name = "日期相减返回分钟")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifMinute(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.minutes());
        return period.getMinutes();
    }

    @ActionMethod(name = "日期时间相减返回小时")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifHour(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.hours());
        return period.getHours();
    }

    @ActionMethod(name = "日期相减返回小时")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifHour(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.hours());
        return period.getHours();
    }

    @ActionMethod(name = "日期时间相减返回天")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifDay(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.days());
        return period.getDays();
    }

    @ActionMethod(name = "日期相减返回天")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifDay(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.days());
        return period.getDays();
    }

    @ActionMethod(name = "日期时间相减返回星期")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifWeek(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.weeks());
        return period.getWeeks();
    }

    @ActionMethod(name = "日期相减返回星期")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifWeek(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.weeks());
        return period.getWeeks();
    }

    @ActionMethod(name = "日期时间相减返回月")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifMonth(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.months());
        return period.getMonths();
    }

    @ActionMethod(name = "日期相减返回月")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifMonth(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.months());
        return period.getMonths();
    }

    @ActionMethod(name = "日期时间相减返回年")
    @ActionMethodParameter(names = {"日期时间", "减去的日期时间"})
    public Object dateDifYear(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.years());
        return period.getYears();
    }

    @ActionMethod(name = "日期相减返回年")
    @ActionMethodParameter(names = {"日期", "减去的日期"})
    public Object dateDifYear(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return null;
        }

        Period period = new Period(d2, d1, PeriodType.years());
        return period.getYears();
    }
}
