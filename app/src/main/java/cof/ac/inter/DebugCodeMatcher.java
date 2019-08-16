package cof.ac.inter;

class DebugCodeMatcher {

    private DebugCodeMatcher() {
    }

    protected static byte getFuctionByte(DebugAction curAction) {
        if (curAction == DebugAction.GERAR_PUMP) {

            return 0x16;
        } else if (curAction == DebugAction.COLD_WATER_INLET) {

            return 0x21;
        } else if (curAction == DebugAction.WASH_MACHINE) {

            return 0x30;
        } else if (curAction == DebugAction.STERILIZE_MACHINE) {

            return 0x31;
        } else if (curAction == DebugAction.DEVIDE_CUP) {

            return 0x32;
        } else if (curAction == DebugAction.OUT_HOTWATER) {

            return 0x33;
        } else if (curAction == DebugAction.OPEN_DOOR) {

            return 0x36;
        } else if (curAction == DebugAction.RESET) {

            return 0x37;
        } else if (curAction == DebugAction.FLUSH_CTR) {

            return 0x38;
        } else if (curAction == DebugAction.OUT_INGREDIENT) {

            return 0x39;
        } else if (curAction == DebugAction.CRUSH_BEAN) {

            return 0x3a;
        } else if (curAction == DebugAction.MOVE_TRAY) {

            return 0x3b;
        } else if (curAction == DebugAction.DOWN_TEA) {

            return 0x3c;
        } else if (curAction == DebugAction.TEA_CTR) {

            return 0x3d;
        } else if (curAction == DebugAction.CUP_MOVE_SYSTEM) {

            return 0x3e;
        } else if (curAction == DebugAction.CTR_LITTLEDOOR) {

            return 0x3f;
        } else if (curAction == DebugAction.CLEAR_WATERBOX) {

            return 0x40;
        } else if (curAction == DebugAction.CLEAR_POT) {

            return 0x41;
        } else if (curAction == DebugAction.TEST_AIRPUMP) {

            return 0x42;
        } else if (curAction == DebugAction.OUT_TEST) {

            return 0x43;
        } else if (curAction == DebugAction.CLEAR_MODULE) {

            return 0x47;
        } else if (curAction == DebugAction.CTR_POLE) {

            return 0x48;
        } else if (curAction == DebugAction.COVER_TEST) {

            return 0x49;
        } else if (curAction == DebugAction.ROTOR_COVER_MOTOR) {

            return 0x4A;
        } else if (curAction == DebugAction.ROTOR_CUP_MOTOR) {

            return 0x4B;
        } else if (curAction == DebugAction.ROTOR_DIAL_90_DEGREES) {

            return 0x4C;
        } else if (curAction == DebugAction.WASTE_MOTOR) {

            return 0x4D;
        } else {

            return 0x33;
        }
    }
}
