/*
 * Copyright 2016 wilqor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wlobs.wilqor.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.model.SearchFilter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends NavigationActivity {
    static final int REQUEST_CODE = 1;

    private Optional<SearchFilter> currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_search, contentFrameLayout);
        ButterKnife.bind(this);
        currentFilter = Optional.absent();
    }

    @OnClick(R.id.search_filter_results_button)
    public void onHandleResultsButtonClick() {
        Intent intent = new Intent(this, SearchFilterActivity.class);
        if (currentFilter.isPresent()) {
            intent.putExtra(SearchFilterActivity.INTENT_FILTER_PARAMETER, currentFilter.get());
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            SearchFilter filter = (SearchFilter) data.getSerializableExtra(SearchFilterActivity.INTENT_FILTER_PARAMETER);
            currentFilter = Optional.fromNullable(filter);
        } else {
            currentFilter = Optional.absent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
