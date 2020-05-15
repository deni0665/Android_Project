package com.example.fontys_students_app

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterSkill(knowledgeList:ArrayList<Knowledge>) : RecyclerView.Adapter<AdapterSkill.ExampleViewHolder>() {

    private var list: ArrayList<Knowledge>? = knowledgeList;
    var ctx: Context? = null

    private var mListener: OnItemClickListener? = null;

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        var v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.student_skills, parent, false)
        var ehv: ExampleViewHolder = AdapterSkill.ExampleViewHolder(v, mListener!!)
        ctx = parent.getContext();
        return ehv; }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        var currentKnowledge: Knowledge = list!!.get(position)

        holder.name.setText(currentKnowledge.getSubjectOfKn() + " - " + currentKnowledge.getLevelOfKn())
    }

    public class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.tvSkills)

        constructor(
            itemView: View,
            ClickListener: AdapterSkill.OnItemClickListener) : this(itemView) {
            itemView.setOnClickListener(object : View.OnClickListener {
                @Override
                override fun onClick(v: View) {
                    if (ClickListener != null) {
                        var position: Int = adapterPosition;
                        if (position != RecyclerView.NO_POSITION) {
                            ClickListener.onItemClick(position);
                            v.isSelected = true
                        }
                    }
                }
            })
        }
    }
    public fun filterList(newlist:ArrayList<Knowledge>)
    {
        list =newlist;
        notifyDataSetChanged();
    }
}
