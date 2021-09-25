package ce.vrgenerator;

import javax.annotation.Nonnull;

/**
 * 参照情報
 * <p>
 * <s> Gradleにより、このクラスの「${}」は書き換えられます </s>
 * <p>
 * ForgeGradle 3で置き換え機能が廃止されたためハードコードされます
 * @author Kamesuta
 */
public class Reference {
	/** Mod ID */
	public static final @Nonnull String MODID = "vrgenerator";
	/** Modの名前 */
	public static final @Nonnull String NAME = "VisibleRayGenerator";
	/** バージョン */
	public static final @Nonnull String VERSION = "5.1.0";
	/** サーバープロキシ */
	public static final @Nonnull String PROXY_SERVER = "ce.vrgenerator.CommonProxy";
	/** クライアントプロキシ */
	public static final @Nonnull String PROXY_CLIENT = "ce.vrgenerator.ClientProxy";
	/** コンフィグGUI */
	public static final @Nonnull String GUI_FACTORY = "ce.vrgenerator.ConfigGuiFactory";
}
