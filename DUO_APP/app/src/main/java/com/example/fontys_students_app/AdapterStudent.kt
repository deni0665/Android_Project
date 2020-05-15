package com.example.fontys_students_app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class AdapterStudent (studentsList:ArrayList<Student>) : RecyclerView.Adapter<AdapterStudent.ExampleViewHolder>() {

    private var list: ArrayList<Student>? = studentsList;
    var ctx: Context? = null

    private var mListener: OnItemClickListener? = null;

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        var v:View= LayoutInflater.from(parent.context).inflate(R.layout.activity_students_list,parent,false)
        var ehv: ExampleViewHolder = AdapterStudent.ExampleViewHolder(v, mListener!!)
        ctx=parent.getContext();
        return ehv;    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        var currentStudent : Student = list!!.get(position)

        holder.mFname.setText(currentStudent.getFirstName());
        holder.mLname.setText((currentStudent.getLastName()));
        holder.mKnowledge.setText(currentStudent.getKnowledgeOf());
        holder.mInterests.setText(currentStudent.getInterestsStudent());

        Glide.with(ctx!!)
            .load(currentStudent.getImage())
            .placeholder(R.drawable.ic_android)
            .into(holder.mImage);
    }

    public class ExampleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mFname = itemView.findViewById<TextView>(R.id.tvFirstNameStudent)
        var mLname = itemView.findViewById<TextView>(R.id.tvSecondNameStudent);
        var mKnowledge = itemView.findViewById<TextView>(R.id.tvKnowledge)
        var mInterests = itemView.findViewById<TextView>(R.id.tvInterests)
        var mImage = itemView.findViewById<ImageView>(R.id.imageView)

        constructor(
            itemView: View,
            ClickListener: AdapterStudent.OnItemClickListener
        ) : this(itemView) {
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
        public fun filterList(newlist:ArrayList<Student>)
        {
            list =newlist;
            notifyDataSetChanged();
        }
}