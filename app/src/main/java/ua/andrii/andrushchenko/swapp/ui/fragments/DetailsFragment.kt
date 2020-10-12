package ua.andrii.andrushchenko.swapp.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val person = args.person

            Glide.with(this@DetailsFragment)
                .load(person.getPersonImagePath())
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

            textViewPersonName.text = person.name
            textViewPersonBirthDate.text = "Birth: ${person.birthYear}"
            textViewPersonHeight.text = "Height: ${person.height}"
            textViewPersonMass.text = "Mass: ${person.mass}"
            textViewPersonHairColor.text = "Hair: ${person.hairColor}"
            textViewPersonSkinColor.text = "Skin: ${person.skinColor}"
            textViewPersonEyeColor.text = "Eye color: ${person.eyeColor}"
            textViewPersonGender.text = "Gender: ${person.gender}"
            textViewPersonHomeWorld.text = person.homeWorld
        }

    }


}