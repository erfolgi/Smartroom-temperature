package com.example.sensorsuhu.view

import com.example.sensorsuhu.model.SuhuModel

interface ManualView{
    fun showItemSuhu(listSuhu : ArrayList<SuhuModel>)
    fun reupdate()
}