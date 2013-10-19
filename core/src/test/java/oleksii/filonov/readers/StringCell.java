package oleksii.filonov.readers;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Calendar;
import java.util.Date;

class StringCell implements Cell {

    private final String value;

    public StringCell(final String value) {
        this.value = value;
    }

    @Override
    public int getColumnIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getRowIndex() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Sheet getSheet() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Row getRow() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellType(final int cellType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCellType() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCachedFormulaResultType() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final double value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final Date value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final Calendar value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final RichTextString value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellFormula(final String formula) throws FormulaParseException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCellFormula() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getNumericCellValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getDateCellValue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RichTextString getRichStringCellValue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getStringCellValue() {
        return value;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellValue(final boolean value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellErrorValue(final byte value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getBooleanCellValue() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte getErrorCellValue() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellStyle(final CellStyle style) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CellStyle getCellStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAsActiveCell() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCellComment(final Comment comment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Comment getCellComment() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeCellComment() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Hyperlink getHyperlink() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setHyperlink(final Hyperlink link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CellRangeAddress getArrayFormulaRange() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPartOfArrayFormulaGroup() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
