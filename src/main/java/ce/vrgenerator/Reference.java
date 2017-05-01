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
	public static final @Nonnull String MODID = "vrgenerator";
	public static final @Nonnull String NAME = "VisibleRayGenerator";
	public static final @Nonnull String VERSION = "${version}";
	public static final @Nonnull String FORGE = "${forgeversion}";
	public static final @Nonnull String MINECRAFT = "${mcversion}";
	public static final @Nonnull String PROXY_SERVER = "ce.vrgenerator.CommonProxy";
	public static final @Nonnull String PROXY_CLIENT = "ce.vrgenerator.ClientProxy";
	public static final @Nonnull String GUI_FACTORY = "ce.vrgenerator.ConfigGuiFactory";
}
