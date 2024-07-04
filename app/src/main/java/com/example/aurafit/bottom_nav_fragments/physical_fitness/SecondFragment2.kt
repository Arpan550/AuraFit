package com.example.aurafit.bottom_nav_fragments.physical_fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.aurafit.databinding.FragmentSecond2Binding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment2 : Fragment() {

    private var _binding: FragmentSecond2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecond2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadGifsChest()
        loadGifsArms()
        loadGifsBack()
        loadGifsCoreAndAbdominalMuscles()
        loadgifsLeg()
        loadgifsfullBody()
    }

    private fun loadgifsfullBody() {
        val burpeeGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/burpee5e81d693ff7a401eab879388b763bf9f.gif?w=1110"
        val mountainClimbersGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/mountain-climbers59e8be018fb64b06a7b0b18149319722.gif?w=1110"
        val bearCrawlsGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/bear-crawls.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(burpeeGifUrl)
            .into(binding.burpeeGif)

        Glide.with(this)
            .asGif()
            .load(mountainClimbersGifUrl)
            .into(binding.mountainClimbersGif)

        Glide.with(this)
            .asGif()
            .load(bearCrawlsGifUrl)
            .into(binding.bearCrawlsGif)
    }

    private fun loadgifsLeg() {
        val jumpSquatsGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/jump-squat.gif?w=1110"
        val reverseLungeGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/reverse-lunge7032753c08e243f09d380d15e1afa9a3.gif?w=1110"
        val donkeykicksGifurl="https://images.everydayhealth.com/images/healthy-living/fitness/donkey-kicks.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(jumpSquatsGifUrl)
            .into(binding.jumpSquatsGif)

        Glide.with(this)
            .asGif()
            .load(reverseLungeGifUrl)
            .into(binding.reverseLungeGif)

        Glide.with(this)
            .asGif()
            .load(donkeykicksGifurl)
            .into(binding.donkeyKicksGif)
    }

    private fun loadGifsCoreAndAbdominalMuscles() {
        val bicycleCrunchesgifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/bicycle-crunch.gif?w=1110"
        val plankGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/plank.gif?w=1110"
        val sidePlankGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/side-plank5e2e280470484037aba9d4c9cef43f80.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(bicycleCrunchesgifUrl)
            .into(binding.bicycleCrunchesGif)

        Glide.with(this)
            .asGif()
            .load(plankGifUrl)
            .into(binding.plankCoreAbdominalGif)

        Glide.with(this)
            .asGif()
            .load(sidePlankGifUrl)
            .into(binding.sidePlankGif)
    }

    private fun loadGifsBack() {
        val supermanYGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/superman-y.gif?w=1110"
        val birdDogGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/bird-dog.gif?w=1110"
        val supermanTGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/superman-t.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(supermanYGifUrl)
            .into(binding.supermanYGif)

        Glide.with(this)
            .asGif()
            .load(birdDogGifUrl)
            .into(binding.birdDogGif)

        Glide.with(this)
            .asGif()
            .load(supermanTGifUrl)
            .into(binding.supermanTGif)
    }

    private fun loadGifsArms() {
        val tricepsDipGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/tricep-dip.gif?w=1110"
        val trianglePushUpsGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/triangle-pushup.gif?w=1110"
        val plankGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/plank-up-down.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(tricepsDipGifUrl)
            .into(binding.tricepsDipGif)

        Glide.with(this)
            .asGif()
            .load(trianglePushUpsGifUrl)
            .into(binding.trianglePushUpGif)

        Glide.with(this)
            .asGif()
            .load(plankGifUrl)
            .into(binding.plankGif)
    }

    private fun loadGifsChest() {
        val pushUpShuffleGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/push-up-shuffle.gif?w=1110"
        val pushUpGifUrl = "https://images.everydayhealth.com/images/healthy-living/fitness/push-up.gif?w=1110"
        val isometricChestSqueezeGifUrl="https://images.everydayhealth.com/images/healthy-living/fitness/isometric-chest-squeeze.gif?w=1110"

        Glide.with(this)
            .asGif()
            .load(pushUpShuffleGifUrl)
            .into(binding.pushUpShuffleGif)

        Glide.with(this)
            .asGif()
            .load(pushUpGifUrl)
            .into(binding.pushUpGif)

        Glide.with(this)
            .asGif()
            .load(isometricChestSqueezeGifUrl)
            .into(binding.isometricChestSqueezeGif)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
