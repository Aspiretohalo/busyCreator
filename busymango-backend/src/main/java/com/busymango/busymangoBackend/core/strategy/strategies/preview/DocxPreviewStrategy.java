package com.busymango.busymangoBackend.core.strategy.strategies.preview;

import com.busymango.busymangoBackend.core.constant.FileConstant;
import com.busymango.busymangoBackend.core.strategy.PreviewStrategy;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class DocxPreviewStrategy implements PreviewStrategy {
    /**
     * 策略唯一可调用的方法
     *
     */
    @Override
    public String preview(String inputFilePath) throws IOException {
        // 创建一个ByteArrayOutputStream来捕获输出
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        convertWordToHtml(inputFilePath, baos);
        // 这里你可以返回htmlOutput作为预览
        return baos.toString();
    }

    // 保存编号计数的映射
    private Map<BigInteger, BigInteger> numberingCountMap = new HashMap<>();

    // 当前页面的累计内容高度
    private double currentPageHeight = 0;
    private int imageCount = 1;

    /**
     * 将 Word 文件转换为 Html
     * 返回html版
     */
    public String convertWordToHtml(String inputFilePath,ByteArrayOutputStream baos) throws IOException {
        FileInputStream fis = new FileInputStream(inputFilePath);
        PrintWriter out = new PrintWriter(baos);
        String outputDirPath = "";

        XWPFDocument document = new XWPFDocument(fis);

        // 页面设置（比如A4页面尺寸）
        CTDocument1 ctDoc = document.getDocument();
        CTSectPr sectPr = ctDoc.getBody().getSectPr();
        CTPageSz pageSize = sectPr.getPgSz();

        /**
         * 获取页面宽度和高度（转换为像素）
         */
        double html_w = convertEmuToHtmlInch(pageSize.getW()) * 96;
        double html_h = convertEmuToHtmlInch(pageSize.getH()) * 96;
        /**
         * 获取页眉页脚
         */
        XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
        XWPFHeader header = headerFooterPolicy.getDefaultHeader();
        XWPFFooter footer = headerFooterPolicy.getDefaultFooter();

        /**
         * 获取页边距
         */
        double top = convertEmuToHtmlInch(sectPr.getPgMar().getTop()) * 96;
        double left = convertEmuToHtmlInch(sectPr.getPgMar().getLeft()) * 96;

        // 页面内容高度
        double pageContentHeight = html_h - top * 2;
        /**
         * 初始化HTML文档
         */
        out.println("<html><head><title>Word to HTML Conversion</title><style>");
        // 定义页眉和页脚的样式
        out.printf(".header { position: absolute; top: 0; width: %fpx;  z-index: 1;margin-top:50px; }", html_w - left * 2);
        out.printf(".footer { position: absolute; bottom: 0; width: %fpx;  z-index: 1; margin-bottom:50px;}", html_w - left * 2);
        out.println("body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background: #eee; }");
        out.printf(".page { width: %fpx; height: %fpx; background: #fff; margin: 10px auto; padding: %fpx %fpx; page-break-after: always; position: relative; overflow: hidden; }", html_w - left * 2, html_h - top * 2, top, left);
        out.println("</style></head><body>");
        // 新页处理逻辑
        out.println("<div class='page'> <!-- Start of the first page -->");
        // 写入页眉
        if (header != null) {
            out.println("<div class='header'>");
            writeHeaderFooter(out, header);
            out.println("</div>");
        }
        // 写入页脚
        if (footer != null) {
            out.println("<div class='footer'>");
            writeHeaderFooter(out, footer);
            out.println("</div>");
        }

        /**
         * 按文档顺序处理段落和图片
         */
        List<IBodyElement> elements = document.getBodyElements();

        for (IBodyElement element : elements) {
            /**
             * 是段落类型（包括图片）则执行
             */
            if (element instanceof XWPFParagraph) {
                processParagraph(out, element, header, footer, pageContentHeight, outputDirPath, 0, 0);
            } else if (element instanceof XWPFTable) {
                /**
                 * 是表格类型则执行
                 */
                processTable(out, (XWPFTable) element, outputDirPath, pageContentHeight);

            }
        }

        // 结束最后一页
        out.println("</div> <!-- End of last page -->");
        out.println("</body></html>");
        out.flush();
        out.close();

        numberingCountMap.clear();
        currentPageHeight = 0;
        return baos.toString("UTF-8");
    }

    /**
     * 会出现部分字体识别不出、编号空格格式错乱问题
     *
     * @param wordFilePath
     * @return
     * @throws IOException
     */
    public String convertWordToPdf(String wordFilePath) throws IOException {
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info("wordFilePath:" + wordFilePath);

        FileInputStream fis = new FileInputStream(wordFilePath);
        XWPFDocument document = new XWPFDocument(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfOptions options = PdfOptions.create();
        PdfConverter.getInstance().convert(document, baos, options);

        // 将字节数组转换为Base64编码的字符串
        String base64Content = Base64.getEncoder().encodeToString(baos.toByteArray());

        fis.close();
        baos.close();

        return base64Content;
    }

    private void processTable(PrintWriter out, XWPFTable table, String outputDirPath, double pageContentHeight) throws UnsupportedEncodingException {
        // 固定表格布局和宽度，单元格宽度为15%
        int tableHeight = getTableHeight(table);

        out.println("<table style=\"border-collapse: collapse; width: 100%; table-layout: fixed;\">");

        for (XWPFTableRow row : table.getRows()) {
            out.println("<tr>");

            for (XWPFTableCell cell : row.getTableCells()) {
                int colSpan = cell.getCTTc().getTcPr().isSetGridSpan() ? cell.getCTTc().getTcPr().getGridSpan().getVal().intValue() : 1;
                int rowSpan = cell.getCTTc().getTcPr().isSetVMerge() ? calculateRowSpan(cell) : 1;

                // 设置每个单元格的宽度
                int cellWidthPercentage = 100 / (table.getRow(0).getTableCells().size());
                out.print("<td");
                if (colSpan > 1) {
                    out.print(" colspan=\"" + colSpan + "\"");
                }
                if (rowSpan > 1) {
                    out.print(" rowspan=\"" + rowSpan + "\"");
                }

                // 添加边框和宽度样式
                String borderStyle = getBorderStyle(cell);
                out.print(" style=\"" + borderStyle + " padding: 5px; width: " + cellWidthPercentage * colSpan + "%;\">");

                // 处理单元格内容
                for (IBodyElement element : cell.getBodyElements()) {
                    if (element instanceof XWPFParagraph) {
                        processParagraph(out, element, null, null, pageContentHeight, outputDirPath, 1, tableHeight);
                    } else if (element instanceof XWPFTable) {
//                        processTable(out, (XWPFTable) element, outputDirPath, pageContentHeight);
                    }
                }
                out.println("</td>");
            }
            out.println("</tr>");
        }
        out.println("</table>");
    }

    private int getTableHeight(XWPFTable table) {
        int totalHeight = 0;

        for (XWPFTableRow row : table.getRows()) {
            // 获取行高（单位为Twips，1 Twip = 1/20 Point）
            if (row.getHeight() > 0) {
                totalHeight += row.getHeight();
            } else {
                // 如果没有设置行高，可以提供一个默认值
                totalHeight += 300; // 假设每行高15pt（1pt = 20 twips）
            }
        }

        return totalHeight;
    }

    private String getBorderStyle(XWPFTableCell cell) {
        StringBuilder style = new StringBuilder();
        // 上、下、左、右边框
        if (cell.getCTTc().getTcPr().isSetTcBorders()) {
            CTTcBorders borders = cell.getCTTc().getTcPr().getTcBorders();
            style.append("border-top: ").append(getBorderWidth(borders.getTop())).append(";");
            style.append("border-bottom: ").append(getBorderWidth(borders.getBottom())).append(";");
            style.append("border-left: ").append(getBorderWidth(borders.getLeft())).append(";");
            style.append("border-right: ").append(getBorderWidth(borders.getRight())).append(";");
        } else {
            style.append("border: 1px solid black;"); // 默认样式
        }
        return style.toString();
    }

    private String getBorderWidth(CTBorder border) {
        if (border != null && border.getSz() != null) {
            int size = border.getSz().intValue();
            return (size / 8) + "pt solid black";
        }
        return "1px solid black"; // 默认边框
    }

    private int calculateRowSpan(XWPFTableCell cell) {
        int rowSpan = 1;
        while (cell.getCTTc().getTcPr().getVMerge().getVal().equals(STMerge.CONTINUE)) {
            rowSpan++;
        }
        return rowSpan;
    }

    private void processParagraph(PrintWriter out, IBodyElement element, XWPFHeader header, XWPFFooter footer, double pageContentHeight, String outputDirPath, int isTable, int tableHeight) throws UnsupportedEncodingException {
        XWPFParagraph paragraph = (XWPFParagraph) element;
        double paragraphHeight = calculateParagraphHeight(paragraph);
        // 分页(不是表格的情况下)
        if (currentPageHeight + paragraphHeight > pageContentHeight && isTable == 0) {
            cutPage(out, header, footer);
//            log.info("分页");
            currentPageHeight = 0;
        }
//        log.info("现在高度:" + currentPageHeight + ":段落高度:" + paragraphHeight + ":页高度:" + pageContentHeight);

        out.print("<p ");
        String paragraphStyleId = paragraph.getStyleID();
        if (paragraphStyleId != null) {
            out.print(" class='" + paragraphStyleId + "'");
        }
        out.print("style='");
        // 获得居中情况
        String alignmentStyle = applyAlignmentStyle(paragraph);
        out.print(alignmentStyle);
        out.print("'");

        out.print(">");
        // 段落样式和编号的代码
        processNumbering(out, paragraph);

        // 段落文章
        for (XWPFRun run : paragraph.getRuns()) {
            if (!run.getEmbeddedPictures().isEmpty()) {
                processImages(out, run, pageContentHeight, header, footer);
            } else {
                /**
                 * 段落文字处理逻辑
                 */
                out.print("<span");
                StringBuilder styleBuilder = new StringBuilder();
                Integer fontSize = run.getFontSize();
                String fontFamily = run.getFontFamily(XWPFRun.FontCharRange.hAnsi);
                String color = run.getColor();
                boolean bold = run.isBold();
                boolean italic = run.isItalic();
                String underline = run.getUnderline().name();

                styleBuilder.append("font-family:").append(fontFamily != null ? fontFamily : "宋体").append(";");
                if (fontSize != null) {
                    styleBuilder.append("font-size:").append(fontSize == -1 ? 12 : fontSize).append("pt;");
                }
                if (color != null) {
                    styleBuilder.append("color:").append(color).append(";");
                }
                if (bold) {
                    styleBuilder.append("font-weight:bold;");
                }
                if (italic) {
                    styleBuilder.append("font-style:italic;");
                }
                if (underline != null) {
                    styleBuilder.append("text-decoration:").append(getUnderline(underline)).append(";");
                }
                if (styleBuilder.length() > 0) {
                    out.print(" style='" + styleBuilder.toString() + getHighlightStyle(run) + "'");
                }
                out.print(">");

                String text = run.getText(0);
                if (text != null) {
                    out.print(text);
                }
                out.print("</span>");
            }

        }
        out.println("</p>");
        // 非表格内段落
        if (paragraphHeight > 0 && paragraph.getText().length() > 0 && isTable == 0) {
            currentPageHeight += paragraphHeight + 16;
        } else {
            // 表格内段落
            currentPageHeight += tableHeight;
        }
    }

    private void processImages(PrintWriter out, XWPFRun run, double pageContentHeight, XWPFHeader header, XWPFFooter footer) throws UnsupportedEncodingException {
        for (XWPFPicture picture : run.getEmbeddedPictures()) {
            XWPFPictureData pictureData = picture.getPictureData();
            String imageExtension = suggestFileExtension(pictureData.getPictureType());
            String imageName = "image" + imageCount + "." + imageExtension;

            // 将图片数据转换为Base64编码
            byte[] imageBytes = pictureData.getData();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            double heightInPx = picture.getDepth();
            // 分页
            if (currentPageHeight + heightInPx > pageContentHeight) {
                cutPage(out, header, footer);
                currentPageHeight = 0;
            }
            // 嵌入Base64编码的图片
            out.print("<img src='data:image/" + imageExtension + ";base64," + base64Image + "' alt='Image' style='height: " + heightInPx + "px;' />");
            currentPageHeight += heightInPx + 16;
            imageCount++;
        }
    }

    private void processNumbering(PrintWriter out, XWPFParagraph paragraph) {
        String numberingText = getNumberingText(paragraph); // 假设这个方法已经实现
        if (numberingText != null) {
            out.print("<span class='numbering' style='");
            StringBuilder styleBuilder = new StringBuilder();
            for (XWPFRun run : paragraph.getRuns()) {
                Integer fontSize = run.getFontSize();
                String fontFamily = run.getFontFamily(XWPFRun.FontCharRange.hAnsi);
                String color = run.getColor();
                boolean bold = run.isBold();
                boolean italic = run.isItalic();
                String underline = run.getUnderline().name();

                styleBuilder.append("font-family:").append(fontFamily != null ? fontFamily : "宋体").append(";");
                if (fontSize != null) {
                    styleBuilder.append("font-size:").append(fontSize == -1 ? 12 : fontSize).append("pt;");
                }
                if (color != null) {
                    styleBuilder.append("color:").append(color).append(";");
                }
                if (bold) {
                    styleBuilder.append("font-weight:bold;");
                }
                if (italic) {
                    styleBuilder.append("font-style:italic;");
                }
                if (underline != null) {
                    styleBuilder.append("text-decoration:").append(getUnderline(underline)).append(";");
                }
            }
            out.print(styleBuilder.toString());
            out.print("'>");

            if (paragraph.getNumFmt() != null &&
                    (paragraph.getNumFmt().equals("decimal") || paragraph.getNumFmt().equals("chineseCounting"))) {
                String numLevelText = paragraph.getNumLevelText();
                if (numLevelText.contains("(")) {
                    numberingText = "(" + numberingText;
                } else if (numLevelText.contains("（")) {
                    numberingText = "（" + numberingText;
                }
                if (numLevelText.contains(")")) {
                    numberingText = numberingText.replace(".", "") + ")";
                } else if (numLevelText.contains("）")) {
                    numberingText = numberingText.replace(".", "") + "）";
                }
            }
            out.print(numberingText + "</span> ");
        }
    }

    /**
     * 获取XWPFRun的高亮颜色。
     *
     * @param run XWPFRun对象
     * @return 高亮颜色的HTML样式字符串，如果没有高亮则返回空字符串
     */
    private static String getHighlightStyle(XWPFRun run) {
        CTRPr rPr = run.getCTR().getRPr();
        if (rPr != null) {
            CTHighlight highlight = rPr.isSetHighlight() ? rPr.getHighlight() : rPr.addNewHighlight();
            if (highlight != null && highlight.getVal() != null) {
                String highlightColor = highlight.getVal().toString();
                return " background-color: " + highlightColor + ";";
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 根据段落的对齐方式生成CSS样式字符串。
     *
     * @param paragraph XWPFParagraph对象
     * @return 包含对齐样式的CSS样式字符串
     */
    private static String applyAlignmentStyle(XWPFParagraph paragraph) {
        ParagraphAlignment alignment = paragraph.getAlignment();
        String alignmentStyle = "";
        switch (alignment) {
            case CENTER:
                alignmentStyle = "text-align: center;";
                break;
            case RIGHT:
                alignmentStyle = "text-align: right;";
                break;
            case BOTH:
            case DISTRIBUTE:
                alignmentStyle = "text-align: justify;";
                break;
            case LEFT:
            default:
                alignmentStyle = "text-align: left;";
                break;
        }
        return alignmentStyle;
    }

    // 写入页眉页脚
    private static void writeHeaderFooter(PrintWriter out, XWPFHeaderFooter headerFooter) {
        List<XWPFParagraph> paragraphs = headerFooter.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            String alignmentStyle = applyAlignmentStyle(paragraph);
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                Integer fontSize = run.getFontSize() != -1 ? run.getFontSize() : 9; // 默认字体大小为9
                String fontFamily = run.getFontFamily(XWPFRun.FontCharRange.hAnsi) != null ? run.getFontFamily(XWPFRun.FontCharRange.hAnsi) : "宋体"; // 默认字体家族为宋体
                boolean bold = run.isBold();
                boolean italic = run.isItalic();

                out.print("<p style=\"");
                out.print(alignmentStyle);
                out.print("margin: 0 auto;");
                out.print("font-family: " + fontFamily + "; ");
                out.print("font-size: " + fontSize + "pt; ");
                out.print("font-weight: " + (bold ? "bold" : "normal") + "; ");
                out.print("font-style: " + (italic ? "italic" : "normal") + "; ");
                out.print("\">");
                out.print(text);
                out.print("</p>");
            }
        }
    }

    private String getNumberingText(XWPFParagraph paragraph) {
        XWPFNumbering numbering = paragraph.getDocument().getNumbering();
        BigInteger numId = paragraph.getNumID();
        BigInteger ilvl = paragraph.getNumIlvl();

        if (numId == null || ilvl == null || numbering == null) {
            return ""; // 无编号则返回空字符串
        }

        try {
            // 确保首次调用时初始化计数器
            if (!numberingCountMap.containsKey(numId)) {
                numberingCountMap.put(numId, BigInteger.ZERO); // 初始化为0
            }

            XWPFNum xwpfNum = numbering.getNum(numId);
            if (xwpfNum == null || xwpfNum.getCTNum() == null) {
                return "";
            }

            BigInteger abstractNumId = xwpfNum.getCTNum().getAbstractNumId().getVal();
            XWPFAbstractNum abstractNum = numbering.getAbstractNum(abstractNumId);
            if (abstractNum == null) {
                return "";
            }

            CTLvl cTLvl = abstractNum.getAbstractNum().getLvlArray(ilvl.intValue());
            if (cTLvl == null) {
                return "";
            }

            // 获取编号格式
            CTNumFmt numFmt = cTLvl.getNumFmt();
            String format = numFmt != null ? numFmt.getVal().toString() : "decimal";

            // 递增编号计数器
            BigInteger currentCount = numberingCountMap.get(numId).add(BigInteger.ONE);
            numberingCountMap.put(numId, currentCount);

            // 根据格式生成编号文本
            return generateNumberingText(format, currentCount);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 生成编号文本的方法
    private String generateNumberingText(String format, BigInteger count) {
        switch (format) {// 1. 2. 3. ...
            case "upperLetter":
                return (char) (count.intValue() - 1 + 'A') + "."; // A. B. C. ...
            case "lowerLetter":
                return (char) (count.intValue() - 1 + 'a') + "."; // a. b. c. ...
            case "chineseCounting":
                return convertToChineseNumber(count.intValue()) + "."; // 一. 二. 三. ...
            case "decimalParentheses":
            case "ordinal":
                return "(" + count.toString() + ")"; // (1) (2) (3) ...
            case "decimalEnclosedCircleChinese":
                return convertToCircleNumber(count.intValue()); // ① ② ③ ...
            case "roman":
                return toRoman(count.intValue()) + "."; // i. ii. iii. ...
            case "decimal":
                // (1), (2), (3)
//            case "lowerRoman":
//                return toLowerRoman(count.intValue()) + "."; // i. ii. iii. ...
            default:
                return count.toString() + "."; // 默认处理
        }
    }

    // 罗马数字转换
    private String toRoman(int number) {
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return (number > 0 && number < romanNumerals.length) ? romanNumerals[number] : String.valueOf(number);
    }

    // 将数字转换为圆圈数字 (①, ②, ③)
    private String convertToCircleNumber(int num) {
        String[] circleNumbers = {"①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳"};
        if (num >= 1 && num <= 20) {
            return circleNumbers[num - 1];
        }
        return String.valueOf(num);  // 超过20的返回普通数字
    }

    // 简单的中文数字转换示例
    private String convertToChineseNumber(int num) {
        // 定义中文数字
        String[] chineseNums = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        if (num <= 0 || num > 99) {
            return "数字超出范围";
        }

        // 处理10以内的数字
        if (num <= 9) {
            return chineseNums[num];
        }

        // 处理10-99的数字
        int tens = num / 10;  // 十位
        int ones = num % 10;  // 个位

        StringBuilder result = new StringBuilder();

        // 如果十位不为0
        if (tens > 1) {
            result.append(chineseNums[tens]);
        }

        // 添加"十"（如果十位大于0，个位为0时也会有"十"）
        if (tens > 0) {
            result.append("十");
        }

        // 处理个位
        if (ones > 0) {
            result.append(chineseNums[ones]);
        }

        return result.toString();
    }

    /**
     * 分页
     *
     * @param out
     * @param header
     * @param footer
     */
    private static void cutPage(PrintWriter out, XWPFHeader header, XWPFFooter footer) {
        out.println("</div> <!-- End of current page -->");
        out.println("<div class='page'> <!-- Start of new page -->");
        // 写入页眉
        if (header != null) {
            out.println("<div class='header'>");
            writeHeaderFooter(out, header);
            out.println("</div>");
        }
        // 写入页脚
        if (footer != null) {
            out.println("<div class='footer'>");
            writeHeaderFooter(out, footer);
            out.println("</div>");
        }
    }

    /**
     * emu转px
     *
     * @param emuValue
     * @return
     */
    private static double convertEmuToPx(double emuValue) {
        // 1 EMU = 1/914400 英寸，1 英寸 = 96 px
        return emuValue / 914400 * 96;
    }

    /**
     * 计算段落的高度。
     *
     * @param paragraph 段落对象
     * @return 段落的高度（以px为单位）
     */
    private double calculateParagraphHeight(XWPFParagraph paragraph) {
        double lineHeight = 1.15; // 默认行高
        int fontSize = 12; // 默认字号
        if (!paragraph.getRuns().isEmpty()) {
            XWPFRun run = paragraph.getRuns().get(0);
            if (run.getFontSize() != -1) {
                fontSize = run.getFontSize();
            }
        }
        return fontSize * lineHeight * 1.3333; // 转换到px
    }

    /**
     * 将拿到的docEMU转换为Html英寸，用于页面尺寸换算（ratio = 真实a4高度（英寸） / 拿到的emu数据转为英寸后的值）。
     */
    private static double convertEmuToHtmlInch(BigInteger emuValue) {
        double inchesFromEmu = emuValue.doubleValue() / FileConstant.EMU_TO_INCH;
        return inchesFromEmu * FileConstant.RATIO_WORD_HTML;
    }

    /**
     * 获取下划线样式。
     */
    private static String getUnderline(String underline) {
        switch (underline) {
            case "SINGLE":
                return "underline";
            case "DOUBLE":
                return "underline double";
            case "NONE":
            default:
                return "none";
        }
    }

    /**
     * 根据图片类型推荐文件扩展名。
     */
    private static String suggestFileExtension(int pictureType) {
        switch (pictureType) {
            case XWPFDocument.PICTURE_TYPE_JPEG:
                return "jpg";
            case XWPFDocument.PICTURE_TYPE_PNG:
                return "png";
            case XWPFDocument.PICTURE_TYPE_GIF:
                return "gif";
            default:
                return "png"; // 默认使用PNG
        }
    }

}
