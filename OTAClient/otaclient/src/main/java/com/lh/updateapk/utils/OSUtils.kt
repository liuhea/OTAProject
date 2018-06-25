package com.lh.updateapk.utils

/**
 * @author liuhe
 * @date ${Date}
 */
//@Throws(IOException::class)
//private fun bootCommand(context: Context, vararg args: String) {
//    RECOVERY_DIR.mkdirs()  // In case we need it
//    COMMAND_FILE.delete()  // In case it's not writable
//    LOG_FILE.delete()
//
//    val command = FileWriter(COMMAND_FILE)
//    try {
//        for (arg in args) {
//            if (!TextUtils.isEmpty(arg)) {
//                command.write(arg)
//                command.write("\n")
//            }
//        }
//    } finally {
//        command.close()
//    }
//
//    // Having written the command file, go ahead and reboot
//    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//    pm.reboot(PowerManager.REBOOT_RECOVERY)
//    throw IOException("Reboot failed (no permissions?)")
//}