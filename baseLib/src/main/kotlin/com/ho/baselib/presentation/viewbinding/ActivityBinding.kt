/*
 * Copyright (c) 2020. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ho.baselib.presentation.viewbinding

import android.app.Activity
import android.view.View
import androidx.viewbinding.ViewBinding

interface ActivityBinding<VB : ViewBinding> {
  val binding: VB
  fun Activity.setContentViewWithBinding(): View
}

class ActivityBindingDelegate<VB : ViewBinding> : ActivityBinding<VB> {
  private lateinit var _binding: VB

  override val binding: VB get() = _binding

  override fun Activity.setContentViewWithBinding(): View {
    _binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    setContentView(_binding.root)
    return _binding.root
  }
}