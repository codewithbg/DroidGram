/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project is only for EDUCATIONAL PURPOSES and IS NOT related / DOES NOT tend
 * to copy any intellectual property of Instagram.
 *
 */

package com.androar.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androar.droidgram.PollView

class MainActivity : AppCompatActivity(), PollView.CardPollWidgetCallBack {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<PollView>(R.id.poll_widget).apply {
            setPollQuestion("Do you like this Library? 😍")
            setPollAnswers("Yes ♥", "No 😥")
            attachCallBackListener(this@MainActivity)
        }
    }

    override fun onOptionOneSelected(pollAnswer: String?) {
        findViewById<PollView>(R.id.poll_widget).apply {
            reset()
            setOptionOnePercentage(90F)
        }
    }

    override fun onOptionTwoSelected(pollAnswer: String?) {
        findViewById<PollView>(R.id.poll_widget).apply {
            reset()
            setOptionTwoPercentage(70F)
        }
    }

}