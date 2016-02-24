/*
 * Copyright 2012 The Android Open Source Project
 * Modifications copyright 2016 Kirill Smirenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ksmirenko.flexicards.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Fragment that represents a single viewed card.
 */
class CardContainerFragment : Fragment() {
    companion object {
        /**
         * The argument representing content of the card's front side.
         */
        val ARG_FRONT_CONTENT = "front"
        /**
         * The argument representing content of the card's back side.
         */
        val ARG_BACK_CONTENT = "back"
        /**
         * The argument representing whether the back side should be shown first
         */
        val ARG_IS_BACK_FIRST = "backfirst"
    }

    private var isShowingBack = false

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?,
            savedInstanceState : Bundle?) : View? {
        val rootView = inflater!!.inflate(R.layout.fragment_cards_view_container, container, false)
        val args = arguments
        isShowingBack = args.getBoolean(ARG_IS_BACK_FIRST)

        // adding card layout
        val cardFragment = CardFrontFragment()
        cardFragment.arguments = args // small workaround
        childFragmentManager
                .beginTransaction()
                .add(R.id.layout_card_container, cardFragment)
                .commit()
        // adding tap event handler
        val layout = rootView.findViewById(R.id.layout_card_container)
        layout.setOnTouchListener { view, motionEvent ->
//            flipCard()
            false
        }

        return rootView
    }

    private fun flipCard() {
        //        if (isShowingBack) {
        //            fragmentManager.popBackStack()
        //            return
        //        }
        val newFragment = if (isShowingBack) CardFrontFragment() else CardBackFragment()
        newFragment.arguments = arguments
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out
                )
                .replace(R.id.layout_card_container, newFragment)
                //                .addToBackStack(null)
                .commit()
        isShowingBack = !isShowingBack
    }

    class CardFrontFragment : Fragment() {
        override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?,
                savedInstanceState : Bundle?) : View? {
            val rootView = inflater!!.inflate(R.layout.fragment_cards_view, container, false)
            val textView = rootView.findViewById(R.id.textview_cardview_mainfield) as TextView
            textView.text = arguments.getString(CardContainerFragment.ARG_FRONT_CONTENT)
            return rootView
        }
    }

    class CardBackFragment : Fragment() {
        override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?,
                savedInstanceState : Bundle?) : View? {
            val rootView = inflater!!.inflate(R.layout.fragment_cards_view, container, false)
            val textView = rootView.findViewById(R.id.textview_cardview_mainfield) as TextView
            textView.text = arguments.getString(CardContainerFragment.ARG_BACK_CONTENT)
            return rootView
        }
    }
}