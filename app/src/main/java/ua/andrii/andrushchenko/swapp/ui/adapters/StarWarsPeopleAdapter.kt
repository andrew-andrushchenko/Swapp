package ua.andrii.andrushchenko.swapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ua.andrii.andrushchenko.swapp.R
import ua.andrii.andrushchenko.swapp.data.Person
import ua.andrii.andrushchenko.swapp.databinding.ItemPersonBinding

class StarWarsPeopleAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Person, StarWarsPeopleAdapter.PersonViewHolder>(PERSON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class PersonViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(person: Person) {
            binding.apply {
                Glide.with(itemView)
                    .load(person.getPersonImagePath())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.person)
                    .into(imageView)

                textViewPersonName.text = person.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(person: Person)
    }

    companion object {
        private val PERSON_COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem == newItem
        }
    }
}