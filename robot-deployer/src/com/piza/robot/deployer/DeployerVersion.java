package com.piza.robot.deployer;

import com.piza.robot.core.Version;

/**
 * Created by Peter on 16/9/26.
 */
public class DeployerVersion extends Version{

    public static final String DEPLOYER_VERSION="1.0.0";


    @Override
    public String getVersion() {
        return "baseVersion:"+VERSION+",robotVersion:"+DEPLOYER_VERSION;
    }
}
