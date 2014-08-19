package eu.artofcoding.beetlejuice.entity;

import static eu.artofcoding.beetlejuice.api.BeetlejuiceConstant.ASTERISK;
import static eu.artofcoding.beetlejuice.api.MimeTypeConstants.*;

public enum MimeType {

    UNKNOWN("Unknown", ASTERISK, APPLICATON_OCTET_STREAM),
    PLAIN("ASCII Text", "txt", TEXT_PLAIN),
    HTML("HTML", "html", TEXT_HTML),
    JPG("JPEG (jpg)", S_JPG, IMAGE_JPEG),
    JPEG("JPEG (jpeg)", S_JPEG, IMAGE_JPEG),
    TIF("TIFF (tif)", S_TIF, IMAGE_TIF),
    TIFF("TIFF (tiff)", S_TIFF, IMAGE_TIFF),
    PNG("Portable Network Graphics (png)", S_PNG, IMAGE_PNG),
    PDF("Portable Document Format (pdf)", S_PDF, APPLICATION_PDF),
    RTF("Rich Text Format (rtf)", S_RTF, APPLICATION_RTF),
    ODT("OpenDocument Writer (odt)", S_ODT, APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT),
    OTT("OpenDocument Writer Template (ott)", S_OTT, APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_TEMPLATE),
    OTH("OpenDocument Writer Web (oth)", S_OTH, APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_WEB),
    ODM("OpenDocument Writer Masterdocument (odm)", S_ODM, APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT_MASTER),
    ODG("OpenDocument Draw (odg)", S_ODG, APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS),
    OTG("OpenDocument Draw Template (otg)", S_OTG, APPLICATION_VND_OASIS_OPENDOCUMENT_GRAPHICS_TEMPLATE),
    ODP("OpenDocument Impress (odp)", S_ODP, APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION),
    OTP("OpenDocument Impress Template (otp)", S_OTP, APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION_TEMPLATE),
    ODS("OpenDocument Calc (ods)", S_ODS, APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET),
    OTS("OpenDocument Calc Template (ots)", S_OTS, APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET_TEMPLATE),
    ODC("OpenDocument Chart (odc)", S_ODC, APPLICATION_VND_OASIS_OPENDOCUMENT_CHART),
    ODF("OpenDocument Formula (odf)", S_ODF, APPLICATION_VND_OASIS_OPENDOCUMENT_FORMULA),
    ODI("OpenDocument Image (odi)", S_ODI, APPLICATION_VND_OASIS_OPENDOCUMENT_IMAGE),
    DOC("Microsoft Office Word 97-2003 (doc)", S_DOC, APPLICATION_MSWORD),
    DOT("Microsoft Office Word 97-2003 Template (dot)", S_DOT, APPLICATION_MSWORD),
    XLS("Microsoft Office Excel 97-2003 (xls)", S_XLS, APPLICATION_VND_MS_EXCEL),
    XLT("Microsoft Office Excel 97-2003 Template (xlt)", S_XLT, APPLICATION_VND_MS_EXCEL),
    XLA("Microsoft Office Excel 97-2003 Addin (xla)", S_XLA, APPLICATION_VND_MS_EXCEL),
    PPT("Microsoft Office PowerPoint 97-2003 (ppt)", S_PPT, APPLICATION_VND_MS_POWERPOINT),
    POT("Microsoft Office PowerPoint 97-2003 Template (pot)", S_POT, APPLICATION_VND_MS_POWERPOINT),
    PPS("Microsoft Office PowerPoint 97-2003 (pps)", S_PPS, APPLICATION_VND_MS_POWERPOINT),
    PPA("Microsoft Office PowerPoint 97-2003 (ppa)", S_PPA, APPLICATION_VND_MS_POWERPOINT),
    DOCX("Microsoft Office Word 2007 (docx)", S_DOCX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT),
    DOTX("Microsoft Office Word 2007 Template (dotx)", S_DOTX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_TEMPLATE),
    DOCM("Microsoft Office Word 2007 (docm)", S_DOCM, APPLICATION_VND_MS_WORD_DOCUMENT_MACRO_ENABLED_12),
    DOTM("Microsoft Office Word 2007 Template (dotm)", S_DOTM, APPLICATION_VND_MS_WORD_TEMPLATE_MACRO_ENABLED_12),
    XLSX("Microsoft Office Excel 2007 (xlsx)", S_XLSX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET),
    XLTX("Microsoft Office Excel 2007 Template (xltx)", S_XLTX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_TEMPLATE),
    XLSM("Microsoft Office Excel 2007 (xlsm)", S_XLSM, APPLICATION_VND_MS_EXCEL_SHEET_MACRO_ENABLED_12),
    XLTM("Microsoft Office Excel 2007 Template (xltm)", S_XLTM, APPLICATION_VND_MS_EXCEL_TEMPLATE_MACRO_ENABLED_12),
    XLAM("Microsoft Office Excel 2007 Addin (xlam)", S_XLAM, APPLICATION_VND_MS_EXCEL_ADDIN_MACRO_ENABLED_12),
    XLSB("Microsoft Office Excel 2007 Binary (xlsb)", S_XLSB, APPLICATION_VND_MS_EXCEL_SHEET_BINARY_MACRO_ENABLED_12),
    PPTX("Microsoft Office PowerPoint 2007 (pptx)", S_PPTX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION),
    POTX("Microsoft Office PowerPoint 2007 Template (potx)", S_POTX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_TEMPLATE),
    PPSX("Microsoft Office PowerPoint 2007 (ppsx)", S_PPSX, APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_SLIDESHOW),
    PPAM("Microsoft Office PowerPoint 2007 Addin (ppam)", S_PPAM, APPLICATION_VND_MS_POWERPOINT_ADDIN_MACRO_ENABLED_12),
    PPTM("Microsoft Office PowerPoint 2007 (pptm)", S_PPTM, APPLICATION_VND_MS_POWERPOINT_PRESENTATION_MACRO_ENABLED_12),
    POTM("Microsoft Office PowerPoint 2007 Template (potm)", S_POTM, APPLICATION_VND_MS_POWERPOINT_TEMPLATE_MACRO_ENABLED_12),
    PPSM("Microsoft Office PowerPoint 2007 Slideshow (ppsm)", S_PPSM, APPLICATION_VND_MS_POWERPOINT_SLIDESHOW_MACRO_ENABLED_12);

    private final String description;
    private final String extension;
    private final String mimeType;

    MimeType(String description, String extension, String mimeType) {
        this.description = description;
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static String getExtensionByMimeType(String mimeType) {
        for (MimeType m : MimeType.values()) {
            if (m.getMimeType().equals(mimeType)) {
                return m.getExtension();
            }
        }
        return null;
    }

    public static String getMimeTypeByExtension(String extension) {
        for (MimeType m : MimeType.values()) {
            if (m.getExtension().equals(extension)) {
                return m.getMimeType();
            }
        }
        return null;
    }

    public static String getDescriptionByExtension(String extension) {
        for (MimeType m : MimeType.values()) {
            if (m.getExtension().equals(extension)) {
                return m.getDescription();
            }
        }
        return null;
    }

}
