package com.theoxao.maikan.utils

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.theoxao.maikan.R
import com.youth.banner.loader.ImageLoader
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.apache.commons.lang3.StringUtils

/**
 * Created by theo on 2018/10/11.
 */
class PicassoRoundCornerImageLoader(private val radius: Int) : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView) {
        var realPath = "default"
        if (path.toString().startsWith("//"))
            realPath = "https:$path"
        else if (StringUtils.isNoneEmpty(path.toString()))
            realPath = path.toString()

        AppUtils.runOnUI {
            Picasso.with(context).load(realPath)
                    .transform(RoundedCornerTransformation(radius)).into(imageView)
        }
    }
}

class PicassoImageLoader() : ImageLoader() {
    override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
        AppUtils.runOnUI {
            when (path) {
                is String -> {
                    var realPath = "default"
                    if (path.startsWith("//"))
                        realPath = "https:$path"
                    else if (StringUtils.isNoneEmpty(path))
                        realPath = path
                    Picasso.with(context).load(realPath
                            ?: "123").placeholder(R.drawable.def_avatar).error(R.drawable.def_avatar).into(imageView)
                }
                is Int -> {
                    Picasso.with(context).load(path).placeholder(R.drawable.def_avatar).error(R.drawable.def_avatar).into(imageView)
                }
            }
        }
    }

    fun displayAvatar(context: Context, path: Any, avatarView: ImageView) {

        AppUtils.runOnUI {
            when (path) {
                is String -> {
                    var realPath = "default"
                    if (path.startsWith("//"))
                        realPath = "https:$path"
                    else if (StringUtils.isNoneEmpty(path))
                        realPath = path
                    Picasso.with(context).load(realPath
                            ?: "123").placeholder(R.drawable.def_avatar).error(R.drawable.def_avatar).fit()
                            .transform(CropCircleTransformation()).into(avatarView)
                }
                is Int -> {
                    Picasso.with(context).load(path).placeholder(R.drawable.def_avatar).error(R.drawable.def_avatar).fit()
                            .transform(CropCircleTransformation()).into(avatarView)
                }
            }
        }
    }

}

class RoundedCornerTransformation(val radius: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val mWidth = source.width
        val mHeight = source.height
        val output = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paintColor = Paint()
        paintColor.flags = Paint.ANTI_ALIAS_FLAG
        val rectF = RectF(Rect(0, 0, mWidth, mHeight))
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paintColor)
        val paintImage = Paint()
        paintImage.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        canvas.drawBitmap(source, 0f, 0f, paintImage)
        source.recycle()
        return output
    }

    override fun key(): String {
        return "rounded_corner"
    }
}