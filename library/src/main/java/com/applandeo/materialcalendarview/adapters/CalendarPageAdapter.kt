package com.applandeo.materialcalendarview.adapters

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.applandeo.materialcalendarview.R
import com.applandeo.materialcalendarview.extensions.CalendarGridView
import com.applandeo.materialcalendarview.listeners.DayRowClickListener
import com.applandeo.materialcalendarview.utils.CalendarProperties
import com.applandeo.materialcalendarview.utils.SelectedDay

import java.util.ArrayList
import java.util.Calendar
import java.util.Date

import com.applandeo.materialcalendarview.utils.CalendarProperties.Companion.CALENDAR_SIZE

/**
 * This class is responsible for loading a calendar page content.
 *
 *
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

class CalendarPageAdapter(
        private val context: Context,
        private val calendarProperties: CalendarProperties
) : PagerAdapter() {
    private var calendarGridView: CalendarGridView? = null

    private var pageMonth: Int = 0

    val selectedDays: List<SelectedDay>
        get() = calendarProperties.selectedDays

    var selectedDay: SelectedDay
        get() = selectedDays.first()
        set(selectedDay) {
            calendarProperties.setSelectedDay(selectedDay)
            informDatePicker()
        }

    init {
        informDatePicker()
    }

    override fun getCount() = CALENDAR_SIZE

    override fun getItemPosition(any: Any) = POSITION_NONE

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        calendarGridView = inflater.inflate(R.layout.calendar_view_grid, null) as CalendarGridView

        loadMonth(position)

        calendarGridView?.onItemClickListener = DayRowClickListener(this,
                calendarProperties, pageMonth)

        container.addView(calendarGridView)
        return calendarGridView as CalendarGridView
    }

    fun addSelectedDay(selectedDay: SelectedDay) {
        if (!calendarProperties.selectedDays.contains(selectedDay)) {
            calendarProperties.selectedDays.add(selectedDay)
            informDatePicker()
            return
        }

        calendarProperties.selectedDays.remove(selectedDay)
        informDatePicker()
    }

    /**
     * This method inform DatePicker about ability to return selected days
     */
    private fun informDatePicker() =
            calendarProperties.onSelectionAbilityListener?.onChange(calendarProperties.selectedDays.size > 0)

    /**
     * This method fill calendar GridView with days
     *
     * @param position Position of current page in ViewPager
     */
    private fun loadMonth(position: Int) {
        val days = ArrayList<Date>()

        // Get Calendar object instance
        val calendar = calendarProperties.firstPageCalendarDate?.clone() as Calendar

        // Add months to Calendar (a number of months depends on ViewPager position)
        calendar.add(Calendar.MONTH, position)

        // Set day of month as 1
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Get a number of the first day of the week
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Count when month is beginning
        val firstDayOfWeek = calendar.firstDayOfWeek
        val monthBeginningCell = (if (dayOfWeek < firstDayOfWeek) 7 else 0) + dayOfWeek - firstDayOfWeek

        // Subtract a number of beginning days, it will let to load a part of a previous month
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        /*
        Get all days of one page (42 is a number of all possible cells in one page
        (a part of previous month, current month and a part of next month))
         */
        while (days.size < 42) {
            days.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        pageMonth = calendar.get(Calendar.MONTH) - 1
        val calendarDayAdapter = CalendarDayAdapter(this, context,
                calendarProperties, days, pageMonth)

        calendarGridView?.adapter = calendarDayAdapter
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}