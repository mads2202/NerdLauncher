package com.mads2202.nerdlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.String
import java.util.*


class NerdLauncherFragment:Fragment() {
    lateinit var mRecyclerView:RecyclerView
    companion object{
    fun newInstance():NerdLauncherFragment{
        return NerdLauncherFragment()
    }
        const val TAG="NerdLauncherFragment"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.nerd_launcher_fragment, container, false)
        mRecyclerView=view.findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager=LinearLayoutManager(activity)
        setupAdapter()
        return view
    }
     inner class ActivityHolder(inflater: LayoutInflater, parent: ViewGroup ) : RecyclerView.ViewHolder(
         inflater.inflate(R.layout.nerd_launcher_item, parent, false)
     ),View.OnClickListener {
        private var mResolveInfo: ResolveInfo? = null
        private val mNameTextView: TextView
        private val mIcon:ImageView
        @RequiresApi(Build.VERSION_CODES.M)
        fun bindActivity(resolveInfo: ResolveInfo?) {
            mResolveInfo = resolveInfo
            val pm: PackageManager = activity!!.packageManager
            val appName = mResolveInfo!!.loadLabel(pm).toString()
            val appIcon=mResolveInfo!!.loadIcon(pm)
            mNameTextView.text = appName
            mIcon.setImageBitmap(appIcon.toBitmap())
            mNameTextView.setOnClickListener(this)
            mIcon.setOnClickListener(this)
        }

        init {
            mNameTextView = itemView.findViewById(R.id.label)
            mIcon=itemView.findViewById(R.id.icon)

        }

         override fun onClick(v: View?) {
             val activityInfo=mResolveInfo?.activityInfo
             val intent= activityInfo?.applicationInfo?.let {
                 Intent(Intent.ACTION_MAIN).setClassName(
                     it.packageName,
                     activityInfo?.name
                 ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             }
             startActivity(intent)
         }
     }
    inner class ActivityAdapter(val mActivities: List<ResolveInfo>):RecyclerView.Adapter<ActivityHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            val inflater=LayoutInflater.from(activity)
            val view=inflater.inflate(R.layout.nerd_launcher_item, parent,false)
            return ActivityHolder(inflater,parent)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            holder.bindActivity(mActivities[position])
        }

        override fun getItemCount(): Int {
          return  mActivities.size
        }

    }

    private fun setupAdapter() {
        val intent=Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager= activity?.packageManager
        val activities:List<ResolveInfo> = packageManager!!.queryIntentActivities(intent, 0)
        Collections.sort(activities) { a, b ->
            val pm = activity!!.packageManager
            String.CASE_INSENSITIVE_ORDER.compare(
                a?.loadLabel(pm).toString(),
                b?.loadLabel(pm).toString()
            )
        }
        Log.i(TAG, "Found " + activities.size + " activities.")
        mRecyclerView.adapter=ActivityAdapter(activities)
    }
}