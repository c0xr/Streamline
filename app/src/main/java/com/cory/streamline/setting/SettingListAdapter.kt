package com.cory.streamline.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cory.streamline.R


class SettingListAdapter(val context: Context, val list: List<Option>) : BaseAdapter() {
    data class Option(val title: String, val subTitle: String?, val iconId: Int)

    class ViewHolder(v: View) {
        val optionTitleText: TextView = v.findViewById(R.id.optionTitle)
        val optionSubtitleText: TextView = v.findViewById(R.id.optionSubtitle)
        val optionIcon: ImageView = v.findViewById(R.id.icon)
    }

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View? {
        var _convertView: View? = convertView
        val holder: ViewHolder
        if (_convertView == null) {
            _convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_view_setting, container, false)
            holder = ViewHolder(_convertView)
            _convertView.tag = holder
        } else {
            holder = _convertView.tag as ViewHolder
        }
        val option = getItem(position) as Option
        holder.apply {
            option.apply {
                optionTitleText.text = title
                if (subTitle != null) {
                    optionSubtitleText.text = subTitle
                    optionSubtitleText.visibility = View.VISIBLE
                }
                optionIcon.setImageResource(iconId)
            }
        }
        _convertView!!.setOnClickListener {
            when (position) {
                1 -> context.startActivity(LayoutCustomActivity.newIntent(context))
                else -> {
                }
            }
        }
        return _convertView
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}