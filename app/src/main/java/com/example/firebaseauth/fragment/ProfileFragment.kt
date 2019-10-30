package com.example.firebaseauth.fragment


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.firebaseauth.R
import com.example.firebaseauth.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private val REQUEST_IMAGE_CAPTURE = 100
    private  lateinit var imageUri :Uri



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)

        image_view.setOnClickListener {
            takePictureIntent()
        }


    }

    private fun takePictureIntent() {
       Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{pictureIntent ->
           pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
               startActivityForResult(pictureIntent,REQUEST_IMAGE_CAPTURE)
           }
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(imageBitmap: Bitmap) {
       val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener{uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE
            if(upload.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        activity?.toast(imageUri.toString())

                        image_view.setImageBitmap(imageBitmap)
                    }
                }
            }else{
                uploadTask.exception?.let {
                    activity?.toast(it.message!!)
                }
            }
        }
    }

}
