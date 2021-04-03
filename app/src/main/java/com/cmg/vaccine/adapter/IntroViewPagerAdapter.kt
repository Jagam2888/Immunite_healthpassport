package com.cmg.vaccine.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.cmg.vaccine.data.IntroItem
import com.cmg.vaccine.R


class IntroViewPagerAdapter(
        private val mContext: Context,
        private val list: List<IntroItem>,
):PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(@NonNull view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.introlayout_design, null)

        val imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_desc)

        Log.e("item",list.get(position).getTitle().toString())
        title.setText(list.get(position).getTitle())
        description.setText(list.get(position).getDescription())
        imgSlide.setImageResource(list.get(position).getScreenImg())


        container.addView(layoutScreen)

        return layoutScreen
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

}