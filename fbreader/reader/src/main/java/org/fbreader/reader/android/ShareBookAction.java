/*
 * Copyright (C) 2012-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.fbreader.reader.android;

import org.fbreader.common.android.FBReaderUtil;
import org.fbreader.reader.AbstractReader;

import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.fbreader.book.Book;

class ShareBookAction extends MainActivity.Action<MainActivity,AbstractReader> {
	ShareBookAction(MainActivity baseActivity) {
		super(baseActivity);
	}

	@Override
	public boolean isVisible() {
		final Book book = BaseActivity.getReader().getCurrentBook();
		return book != null && ZLFile.createFileByPath(book.getPath()).getPhysicalFile() != null;
	}

	@Override
	protected void run(Object ... params) {
		FBReaderUtil.shareBook(BaseActivity, BaseActivity.getReader().getCurrentBook());
	}
}