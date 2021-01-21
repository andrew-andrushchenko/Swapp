package ua.andrii.andrushchenko.swapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ua.andrii.andrushchenko.swapp.R
import ua.andrii.andrushchenko.swapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val person = args.person

            Glide.with(this@DetailsFragment)
                .load(person.personImagePath)
                .error(R.drawable.person)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewPersonName.isVisible = true
                        textViewPersonBirthDate.isVisible = true
                        textViewPersonHeight.isVisible = true
                        textViewPersonMass.isVisible = true
                        textViewPersonHairColor.isVisible = true
                        textViewPersonSkinColor.isVisible = true
                        textViewPersonEyeColor.isVisible = true
                        textViewPersonGender.isVisible = true
                        textViewPersonHomeWorld.isVisible = true
                        return false
                    }
                })
                .into(imageView)

            (requireActivity() as AppCompatActivity).supportActionBar?.title = person.name

            textViewPersonName.text = person.name
            textViewPersonBirthDate.text =
                "${resources.getString(R.string.person_birth)} ${person.birthYear}"
            textViewPersonHeight.text =
                "${resources.getString(R.string.person_height)} ${person.height} ${resources.getString(R.string.person_cm)}"
            textViewPersonMass.text =
                "${resources.getString(R.string.person_mass)} ${person.mass} ${resources.getString(R.string.person_kg)}"
            textViewPersonHairColor.text =
                "${resources.getString(R.string.person_hair)} ${person.hairColor}"
            textViewPersonSkinColor.text =
                "${resources.getString(R.string.person_skin)} ${person.skinColor}"
            textViewPersonEyeColor.text =
                "${resources.getString(R.string.person_eye_color)} ${person.eyeColor}"
            textViewPersonGender.text =
                "${resources.getString(R.string.person_gender)} ${person.gender}"
            textViewPersonHomeWorld.text = person.homeWorld
        }

    }


}