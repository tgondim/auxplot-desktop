package br.com.tg.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import br.com.tg.entidades.Plotagem;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class PlotagensTableModel extends AbstractTableModel {

	public static final int ID_INDEX = 0;
	public static final int DESCRICAO_INDEX = 1;
	public static final int UNIDADE_INDEX = 2;
	public static final int VALOR_INDEX = 3;

	protected String[] columnNames;
	protected Vector<Object> dataVector;
	
	public PlotagensTableModel(String[] newColumnNames) {
        this.columnNames = newColumnNames;
        this.dataVector = new Vector<Object>();
	}
	
	public String getColumnNames(int column) {
		return columnNames[column];
	}

    public boolean isCellEditable(int row, int column) {
        if (column == ID_INDEX || column == DESCRICAO_INDEX
        		|| column == UNIDADE_INDEX
        		|| column == VALOR_INDEX) return false;
        else return true;
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case ID_INDEX:
            	return Integer.class;
            case DESCRICAO_INDEX:
            	return String.class;
            case UNIDADE_INDEX:
            	return String.class;
            case VALOR_INDEX:
            	return String.class;
            default:
               return Object.class;
        }
    }
    
    @Override
    public Object getValueAt(int row, int column) {
        Plotagem plotagem = (Plotagem)dataVector.get(row);
        switch (column) {
            case ID_INDEX:
            	return plotagem.getId();
            case DESCRICAO_INDEX:
            	return plotagem.getDescricao();
            case UNIDADE_INDEX:
            	return plotagem.getUnidade();
            case VALOR_INDEX:
            	return Validador.inserirVirgula(plotagem.getValor());
            default:
        }
        return new Object();
    }
    
    public void setValueAt(Object value, int row, int column) {
    	Plotagem plotagem = (Plotagem)dataVector.get(row);
        switch (column) {
        case ID_INDEX:
        	plotagem.setId((Integer)value);
        	break;
        case DESCRICAO_INDEX:
        	plotagem.setDescricao((String)value);
        	break;
        case UNIDADE_INDEX:
        	plotagem.setUnidade((String)value);
        	break;
        case VALOR_INDEX:
        	plotagem.setValor((Float)value);
        	break;
        default:
        	System.out.println("invalid index");
        }
        fireTableCellUpdated(row, column);
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        Plotagem plotagem = (Plotagem)dataVector.get(dataVector.size() - 1);
        if (plotagem.getDescricao().trim().equals("") &&
        		plotagem.getUnidade().trim().equals("") &&
        		plotagem.getValor() == 0) 
        {
           return true;
        }
        else return false;
    }

    public void addEmptyRow() {
        dataVector.add(new Plotagem());
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }
    
    public void addRow(Plotagem newPlotagem) {
    	dataVector.add(newPlotagem);
    	fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }
    
    public void removeRow(int row) {
    	dataVector.removeElementAt(row);
    }
    
    public void clearTable() {
    	dataVector = new Vector<Object>();
    }

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}
	
	@Override
	public String getColumnName(int column){  
		return columnNames[column];  
	}
	
	public boolean isEmpty() {
		if (dataVector.firstElement() == null) {
			return true;
		}
		return false;
	}
	
	public int getRow(Plotagem plotagem) {
		int indice = dataVector.indexOf(plotagem);
		return indice;
	}
	
	public List<Plotagem> getList() {
		List<Plotagem> listaPlotagem = new ArrayList<Plotagem>();
		for (Iterator<Object> i = dataVector.iterator(); i.hasNext();) {
			listaPlotagem.add((Plotagem)i.next());
		}
		return listaPlotagem;
	}


}
