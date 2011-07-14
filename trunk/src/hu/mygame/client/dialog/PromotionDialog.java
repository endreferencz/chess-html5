package hu.mygame.client.dialog;

import hu.mygame.client.chessboard.ActionHandler;
import hu.mygame.shared.PromotionPiece;
import hu.mygame.shared.exceptions.IllegalOperationException;
import hu.mygame.shared.moves.Move;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class PromotionDialog extends DialogBox {

	interface PromotionDialogUiBinder extends UiBinder<Widget, PromotionDialog> {
	}
	private static final PromotionDialogUiBinder uiBinder = GWT.create(PromotionDialogUiBinder.class);
	private ActionHandler actionHandler = null;
	@UiField
	PushButton button_bishop;
	@UiField
	PushButton button_knight;

	@UiField
	PushButton button_queen;
	@UiField
	PushButton button_rook;
	@UiField
	DialogBox dialog;
	@UiField
	Label label;

	private Move move = null;

	public PromotionDialog(Move move, ActionHandler actionHandler) {
		setWidget(uiBinder.createAndBindUi(this));
		this.move = move;
		this.actionHandler = actionHandler;
		dialog.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				dialog.setPopupPosition((Window.getClientWidth() - offsetWidth) / 2,
						(Window.getClientHeight() - offsetHeight) / 2);
			}
		});
	}

	@UiHandler("button_bishop")
	void onBishop(ClickEvent e) {
		dialog.hide();
		try {
			move.setPromotionPiece(PromotionPiece.BISHOP);
			actionHandler.makeMove(move);
		} catch (IllegalOperationException e1) {
			// TODO
		}
	}
	@UiHandler("button_knight")
	void onKnight(ClickEvent e) {
		dialog.hide();
		try {
			move.setPromotionPiece(PromotionPiece.KNIGHT);
			actionHandler.makeMove(move);
		} catch (IllegalOperationException e1) {
			// TODO
		}
	}
	@UiHandler("button_queen")
	void onQueen(ClickEvent e) {
		dialog.hide();
		try {
			move.setPromotionPiece(PromotionPiece.QUEEN);
			actionHandler.makeMove(move);
		} catch (IllegalOperationException e1) {
			// TODO
		}
	}
	@UiHandler("button_rook")
	void onRook(ClickEvent e) {
		dialog.hide();
		try {
			move.setPromotionPiece(PromotionPiece.ROOK);
			actionHandler.makeMove(move);
		} catch (IllegalOperationException e1) {
			// TODO
		}
	}
}
