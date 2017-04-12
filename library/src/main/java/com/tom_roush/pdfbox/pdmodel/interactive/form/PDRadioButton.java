package com.tom_roush.pdfbox.pdmodel.interactive.form;

import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSDictionary;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.common.COSObjectable;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;

import java.io.IOException;
import java.util.List;

/**
 * Radio button fields contain a set of related buttons that can each be on or off.
 *
 * @author sug
 */
public final class PDRadioButton extends PDButton
{
    /**
     * An Ff flag.
     */
    private static final int FLAG_NO_TOGGLE_TO_OFF = 1 << 14;

    /**
     * @see PDField#PDField(PDAcroForm)
     *
     * @param acroForm The acroform.
     */
    public PDRadioButton(PDAcroForm acroForm)
    {
        super(acroForm);
        setRadioButton(true);
    }

    /**
     * Constructor.
     *
     * @param acroForm The form that this field is part of.
     * @param field the PDF object to represent as a field.
     * @param parent the parent node of the node
     */
    PDRadioButton(PDAcroForm acroForm, COSDictionary field, PDNonTerminalField parent)
    {
        super(acroForm, field, parent);
    }

    /**
     * From the PDF Spec <br/>
     * If set, a group of radio buttons within a radio button field that use the same value for the on state will turn
     * on and off in unison; that is if one is checked, they are all checked. If clear, the buttons are mutually
     * exclusive (the same behavior as HTML radio buttons).
     *
     * @param radiosInUnison The new flag for radiosInUnison.
     */
    public void setRadiosInUnison(boolean radiosInUnison)
    {
        dictionary.setFlag(COSName.FF, FLAG_RADIOS_IN_UNISON, radiosInUnison);
    }

    /**
     * @return true If the flag is set for radios in unison.
     */
    public boolean isRadiosInUnison()
    {
        return dictionary.getFlag(COSName.FF, FLAG_RADIOS_IN_UNISON);
    }

    /**
     * This will get the export value.
     * <p>
     * A RadioButton might have an export value to allow field values
     * which can not be encoded as PDFDocEncoding or for the same export value
     * being assigned to multiple RadioButtons in a group.<br/>
     * To define an export value the RadioButton must define options {@link #setOptions(List)}
     * which correspond to the individual items within the RadioButton.</p>
     * <p>
     * The method will either return the value from the options entry or in case there
     * is no such entry the fields value</p>
     *
     * @return the export value of the field.
     * @throws IOException in case the fields value can not be retrieved
     */
    public String getExportValue() throws IOException
    {
        List<String> options = getOptions();
        if (options.isEmpty())
        {
            return getValue();
        }
        else
        {
            String fieldValue = getValue();
            List<PDAnnotationWidget> kids = getWidgets();
            int idx = 0;
            for (COSObjectable kid : kids)
            {
                // fixme: this is always false, because it's kids are always widgets, not fields.
                if (kid instanceof PDCheckbox)
                {
                    PDCheckbox btn = (PDCheckbox) kid;
                    if (btn.getOnValue().equals(fieldValue))
                    {
                        break;
                    }
                    idx++;
                }
            }
            if (idx <= options.size())
            {
                return options.get(idx);
            }
        }
        return "";
    }

    @Override
    public String getValue() throws IOException
    {
        COSBase attribute = getInheritableAttribute(COSName.V);

        if (attribute == null)
        {
            return "";
        }
        else if (attribute instanceof COSName)
        {
            return ((COSName) attribute).getName();
        }
        else
        {
            throw new IOException("Expected a COSName entry but got " + attribute.getClass().getName());
        }
    }

    @Override
    public void setValue(String fieldValue)
    {
        COSName nameForValue = COSName.getPDFName(fieldValue);
        dictionary.setItem(COSName.V, nameForValue);
        for (PDAnnotationWidget widget : getWidgets())
        {
            PDAppearanceEntry appearanceEntry = widget.getAppearance().getNormalAppearance();
            if (((COSDictionary) appearanceEntry.getCOSObject()).containsKey(nameForValue))
            {
                widget.getCOSObject().setName(COSName.AS, fieldValue);
            }
            else
            {
                widget.getCOSObject().setItem(COSName.AS, COSName.OFF);
            }
        }
    }
}