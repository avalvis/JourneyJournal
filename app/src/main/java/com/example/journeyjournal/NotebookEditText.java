package com.example.journeyjournal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

// This is a custom EditText class, named NotebookEditText, which is designed to draw
// horizontal lines across the height of the EditText.
// This gives the EditText a lined paper or notebook-like appearance.
public class NotebookEditText extends androidx.appcompat.widget.AppCompatEditText {
    // Paint object to draw lines
    private Paint linePaint;

    // Constructor
    public NotebookEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize the Paint object
        linePaint = new Paint();
        // Set the color of the line
        linePaint.setColor(ContextCompat.getColor(context, R.color.dark_teal));
        // Set the width of the line
        linePaint.setStrokeWidth(2f);
    }

    // Override the onDraw method to draw lines
    @Override
    protected void onDraw(Canvas canvas) {
        // Get the height of the view
        int height = getHeight();
        // Get the height of a line
        int lineHeight = getLineHeight();
        // Calculate the number of lines
        int numberOfLines = height / lineHeight;

        // Draw lines for each line of text
        for (int i = 0; i < numberOfLines; i++) {
            // Calculate the Y position of the line
            float lineY = lineHeight * (i + 1) + getPaddingTop();
            // Draw the line
            canvas.drawLine(getPaddingLeft(), lineY, getWidth() - getPaddingRight(), lineY, linePaint);
        }

        // Call the superclass's onDraw method
        super.onDraw(canvas);
    }
}