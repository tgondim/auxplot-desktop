package br.com.tg.gui.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import br.com.tg.entidades.Fatura;
import br.com.tg.entidades.Pessoa;
import br.com.tg.entidades.StatusFatura;
import br.com.tg.util.Conversor;
import br.com.tg.util.Validador;

@SuppressWarnings("serial")
public class FaturasTableModel extends AbstractTableModel {

	public static final int ID_INDEX = 0;
	public static final int NOME_PESSOA_INDEX = 1;
	public static final int DATA_EMISSAO_INDEX = 2;
	public static final int DATA_VENCIMENTO_INDEX = 3;
	public static final int STATUS_FATURA_INDEX = 4;
	public static final int VALOR_TOTAL_INDEX = 5;

	protected String[] columnNames;
	protected Vector<Object> dataVector;
	
	public FaturasTableModel(String[] newColumnNames) {
        this.columnNames = newColumnNames;
        this.dataVector = new Vector<Object>();
	}
	
	public String getColumnNames(int column) {
		return columnNames[column];
	}

    public boolean isCellEditable(int row, int column) {
    	return false;
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case ID_INDEX:
            	return Integer.class;
            case NOME_PESSOA_INDEX:
            	return JComboBox.class;
            case DATA_EMISSAO_INDEX:
            	return String.class;
            case DATA_VENCIMENTO_INDEX:
            	return String.class;
            case STATUS_FATURA_INDEX:
            	return StatusFatura.class;
            case VALOR_TOTAL_INDEX:
            	return String.class;
            default:
               return Object.class;
        }
    }
    
    @Override
    public Object getValueAt(int row, int column) {
    	Fatura fatura = new Fatura();
    	if (row >= 0) {
    		fatura = (Fatura)dataVector.get(row);
    	}
        switch (column) {
            case ID_INDEX:
            	return fatura.getId();
            case NOME_PESSOA_INDEX:
            	return fatura.getPessoaPai();
            case DATA_EMISSAO_INDEX:
            	return CalendarFormatter.formatDate(fatura.getDataEmissao());
            case DATA_VENCIMENTO_INDEX:
            	return CalendarFormatter.formatDate(fatura.getDataVencimento());
            case STATUS_FATURA_INDEX:
            	return fatura.getStatusFatura();
            case VALOR_TOTAL_INDEX:
            	return Validador.inserirVirgula(fatura.getValorTotal());
            default:
        }
        return new Object();
    }
    
    public void setValueAt(Object value, int row, int column) {
    	Fatura fatura = (Fatura)dataVector.get(row);
        switch (column) {
        case ID_INDEX:
        	fatura.setId((Integer)value);
        	break;
        case NOME_PESSOA_INDEX:
        	fatura.setPessoaPai((Pessoa)value);
        	break;
        case DATA_EMISSAO_INDEX:
        	try {
				fatura.setDataEmissao(Conversor.stringToCalendar((String)value));
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse da data de emissão.");
				e.printStackTrace();
			}
        	break;
        case DATA_VENCIMENTO_INDEX:
        	try {
				fatura.setDataVencimento(Conversor.stringToCalendar((String)value));
			} catch (ParseException e) {
				System.out.println("Erro ao executar o parse da data de vencimento.");
				e.printStackTrace();
			}
        	break;
        case STATUS_FATURA_INDEX:
        	fatura.setStatusFatura((StatusFatura)value);
        	break;
        case VALOR_TOTAL_INDEX:
        	fatura.setValorTotal(Validador.removerVirgula((String)value));
        	break;
        default:
        	System.out.println("invalid index");
        }
        fireTableCellUpdated(row, column);
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) return false;
        Fatura fatura = (Fatura)dataVector.get(dataVector.size() - 1);
        if (fatura.getPessoaPai().getNome().trim().equals("") &&
        		fatura.getProjeto().trim().equals("") &&
        		fatura.getValorTotal() == 0) 
        {
           return true;
        }
        else return false;
    }

    public void addEmptyRow() {
        dataVector.add(new Fatura());
        fireTableRowsInserted(
           dataVector.size() - 1,
           dataVector.size() - 1);
    }
    
    public void addRow(Fatura newFatura) {
    	dataVector.add(newFatura);
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
	
	public int getRow(Fatura fatura) {
		int indice = dataVector.indexOf(fatura);
		return indice;
	}
	
	public List<Fatura> getList() {
		List<Fatura> listaFatura = new ArrayList<Fatura>();
		for (Iterator<Object> i = dataVector.iterator(); i.hasNext();) {
			listaFatura.add((Fatura)i.next());
		}
		return listaFatura;
	}


}
