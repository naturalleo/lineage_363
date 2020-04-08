package com.lineage.server.templates;

import java.util.ArrayList;
import java.util.Random;

import com.lineage.server.model.weaponskill.L1WSkillExecutor;
import com.lineage.server.model.weaponskill.L1WSprExecutor;

/**
 * 武器技能数据暂存 制作中 daien 2012-05-12
 * 
 * @author daien
 * 
 */
public class L1WeaponSkill {

    private final static Random _random = new Random();

    private int _weapon_id;// 编号

    private int _random_skill;// 发动机率(1/1000)

    private int _dmg_count;// 伤害次数(基础设置1 大于1将会伤害多次(设定次数))

    private boolean _dmg_mr = false;// 伤害是否受到抗魔影响减低

    private boolean _dmg_ac = false;// 伤害是否受到防御影响减低

    private boolean _dmg_int = false;// 伤害是否受使用者智力影响提高

    private boolean _dmg_str = false;// 伤害是否受使用者力量影响提高

    private boolean _dmg_dex = false;// 伤害是否受使用者敏捷影响提高

    private ArrayList<L1WSkillExecutor> _powerList = null;

    private ArrayList<L1WSprExecutor> _sprList = null;

    public ArrayList<L1WSkillExecutor> get_powerList() {
        return _powerList;
    }

    public void set_powerList(ArrayList<L1WSkillExecutor> powerList) {
        this._powerList = powerList;
    }

    public ArrayList<L1WSprExecutor> get_sprList() {
        return _sprList;
    }

    public void set_sprList(ArrayList<L1WSprExecutor> sprList) {
        this._sprList = sprList;
    }

}
