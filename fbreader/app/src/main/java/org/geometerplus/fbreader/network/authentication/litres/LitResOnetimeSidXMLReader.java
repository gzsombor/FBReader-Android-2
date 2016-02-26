/*
 * Copyright (C) 2010-2015 FBReader.ORG Limited <contact@fbreader.org>
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

package org.geometerplus.fbreader.network.authentication.litres;

import org.geometerplus.zlibrary.core.xml.ZLStringMap;
import org.geometerplus.zlibrary.core.network.ZLNetworkAuthenticationException;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;

import org.geometerplus.fbreader.network.NetworkException;

class LitResOnetimeSidXMLReader extends LitResAuthenticationXMLReader {
	private static final String TAG_AUTHORIZATION_FAILED = "catalit-authorization-failed";
	private static final String TAG_GOT_SID = "catalit_get_onetime_sid";

	public String Sid;

	@Override
	public boolean startElementHandler(String tag, ZLStringMap attributes) {
		switch (tag.toLowerCase().intern()) {
			case TAG_AUTHORIZATION_FAILED:
				setException(new ZLNetworkAuthenticationException());
				break;
			case TAG_GOT_SID:
				Sid = attributes.getValue("otsid");
				break;
			default:
				setException(ZLNetworkException.forCode(NetworkException.ERROR_SOMETHING_WRONG, LitResUtil.HOST_NAME));
				break;
		}
		return true;
	}
}
