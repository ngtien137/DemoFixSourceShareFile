/*
 * Copyright (C) 2021 Veli Tasalı
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.monora.uprotocol.client.android.database.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.monora.uprotocol.client.android.model.ListItem

@Parcelize
@Entity(
    tableName = "webTransfer"
)
data class WebTransfer(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uri: Uri,
    val name: String,
    val mimeType: String,
    val size: Long,
    val dateCreated: Long = System.currentTimeMillis(),
) : Parcelable, ListItem {
    override val listId: Long
        get() = uri.hashCode().toLong() + javaClass.hashCode()
}
