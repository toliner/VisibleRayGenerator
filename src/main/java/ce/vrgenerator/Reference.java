package ce.vrgenerator;

import javax.annotation.Nonnull;

/**
 * 参照情報
 * <p>
 * Gradleにより、このクラスの「${}」は書き換えられます
 *
 * @author Kamesuta
 */
public class Reference {
	/** Mod ID */
	public static final @Nonnull String MODID = "vrgenerator";
	/** Modの名前 */
	public static final @Nonnull String NAME = "VisibleRayGenerator";
	/** バージョン ※書き換えられます */
	public static final @Nonnull String VERSION = "${version}";
	/** Forgeバージョン ※書き換えられます */
	public static final @Nonnull String FORGE = "${forgeversion}";
	/** Minecraftバージョン ※書き換えられます */
	public static final @Nonnull String MINECRAFT = "${mcversion}";
	/** サーバープロキシ */
	public static final @Nonnull String PROXY_SERVER = "ce.vrgenerator.CommonProxy";
	/** クライアントプロキシ */
	public static final @Nonnull String PROXY_CLIENT = "ce.vrgenerator.ClientProxy";
	/** コンフィグGUI */
	public static final @Nonnull String GUI_FACTORY = "ce.vrgenerator.ConfigGuiFactory";
}
