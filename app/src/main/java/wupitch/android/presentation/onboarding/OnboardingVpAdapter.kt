package wupitch.android.presentation.onboarding

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import wupitch.android.databinding.ItemOnboardingBinding
import wupitch.android.domain.model.OnboardingContent

class OnboardingVpAdapter(
    private val context : Context,
    private val onBoardingContentList : ArrayList<OnboardingContent>
    ) : RecyclerView.Adapter<OnboardingVpAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvOnboardingTitle.text = Html.fromHtml(onBoardingContentList[position].title)
        holder.binding.tvOnboardingSubtitle.text = onBoardingContentList[position].subtitle
        holder.binding.ivOnboarding.setImageDrawable(
            ContextCompat.getDrawable(context, onBoardingContentList[position].imgDrawable)
        )
    }

    override fun getItemCount(): Int = onBoardingContentList.size


}