package wupitch.android.presentation.onboarding

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import wupitch.android.databinding.ItemOnboardingBinding
import wupitch.android.domain.model.OnboardingContent
import android.widget.LinearLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.setMargins
import wupitch.android.R


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
        holder.binding.tvOnboardingTitle.text =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)  Html.fromHtml(onBoardingContentList[position].title,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            else Html.fromHtml(onBoardingContentList[position].title)

        holder.binding.tvOnboardingSubtitle.text = onBoardingContentList[position].subtitle
        holder.binding.ivOnboarding.setImageDrawable(
            ContextCompat.getDrawable(context, onBoardingContentList[position].imgDrawable)
        )
        if(position ==1) {
            val params = holder.binding.ivOnboarding.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 0, 0, 0)
            holder.binding.ivOnboarding.layoutParams = params
        }
    }

    override fun getItemCount(): Int = onBoardingContentList.size


}