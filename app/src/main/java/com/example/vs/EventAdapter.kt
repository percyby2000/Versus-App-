package com.example.vs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vs.databinding.ItemEventBinding

class EventsAdapter(private val onEventClick: (Event) -> Unit) :
    ListAdapter<Event, EventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.apply {
                tvEventName.text = event.eventName
                tvEventDuration.text = "Duración: ${event.duration}"
                tvEventPrice.text = "Precio: $${event.price}"
                tvEventLocation.text = "Ubicación: ${event.location}"

                root.setOnClickListener { onEventClick(event) }
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Event, newItem: Event) =
            oldItem == newItem
    }
}