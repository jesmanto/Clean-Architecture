/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.majesticreader.presentation

import android.os.Bundle
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.android.majesticreader.domain.Document
import com.raywenderlich.android.majesticreader.R
import com.raywenderlich.android.majesticreader.RoomDocumentDataSource
import com.raywenderlich.android.majesticreader.data.BookmarkRepository
import com.raywenderlich.android.majesticreader.data.DocumentRepository
import com.raywenderlich.android.majesticreader.framework.InMemoryOpenDocumentDataSource
import com.raywenderlich.android.majesticreader.framework.Interactors
import com.raywenderlich.android.majesticreader.framework.MajesticViewModelFactory
import com.raywenderlich.android.majesticreader.framework.RoomBookmarkDataSource
import com.raywenderlich.android.majesticreader.interactors.*
import com.raywenderlich.android.majesticreader.presentation.library.LibraryFragment
import com.raywenderlich.android.majesticreader.presentation.reader.ReaderFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainActivityDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookmarkRepository = BookmarkRepository(RoomBookmarkDataSource(this))
        val documentRepository = DocumentRepository(
            RoomDocumentDataSource(this),
            InMemoryOpenDocumentDataSource()
        )

        MajesticViewModelFactory.inject(
            this,
            Interactors(
                AddBookmark(bookmarkRepository),
                GetBookmarks(bookmarkRepository),
                RemoveBookmark(bookmarkRepository),
                AddDocument(documentRepository),
                GetDocuments(documentRepository),
                RemoveDocument(documentRepository),
                GetOpenDocument(documentRepository),
                SetOpenDocument(documentRepository)
            )
        )
    }



    override fun onBackPressed() {
    val drawerLayout: DrawerLayout = findViewById(
        R.id.drawer_layout)
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.nav_library -> supportFragmentManager.beginTransaction()
          .replace(R.id.content, LibraryFragment.newInstance())
          .commit()
      R.id.nav_reader -> openDocument(
          Document.EMPTY)
    }
    val drawerLayout: DrawerLayout = findViewById(
        R.id.drawer_layout)
    drawerLayout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun openDocument(document: Document) {
    nav_view.menu.findItem(R.id.nav_reader).isChecked = true
    supportFragmentManager.beginTransaction()
        .replace(R.id.content, ReaderFragment.newInstance(document))
        .commit()
  }
}
