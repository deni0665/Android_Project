package com.example.fontys_students_app

class Knowledge(s:String,l:Level) {

    var subject:String = s;
    var level:Level = l;

    public fun getSubjectOfKn():String
    {
        return subject
    }

    public fun getLevelOfKn():Level
    {
        return level
    }

    public fun changeLevelToPro(level1:Level)
    {
            level = Level.PRO
    }

    override fun toString(): String {
       return subject + " - " + level
    }
}